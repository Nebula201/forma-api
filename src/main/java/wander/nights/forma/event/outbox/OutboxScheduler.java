package wander.nights.forma.event.outbox;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import wander.nights.forma.event.outbox.OutboxEvent.Status;

import java.time.Instant;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.abbreviate;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxScheduler {

    private static final int BATCH_SIZE = 50;
    private static final int DELAY_SECONDS = 5;

    private final OutboxEventRepository outboxEventRepository;

    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void publishPendingEvents() {
        Instant threshold = Instant.now().minusSeconds(DELAY_SECONDS);
        List<OutboxEvent> events = outboxEventRepository.findByStatusAndEventAtBeforeOrderByEventAtAsc(
                Status.PENDING, threshold, PageRequest.of(0, BATCH_SIZE));

        for (OutboxEvent event : events) {
            try {
                deliver(event);
                event.setStatus(Status.SENT);
                event.setPublishedAt(Instant.now());
            } catch (Exception e) {
                log.error("Failed to deliver event: type={}, eventId={}", event.getEventType(), event.getEventId(), e);
                event.setRetryCount(event.getRetryCount() + 1);
                event.setFailedReason(abbreviate(e.getClass().getSimpleName() + ": " + e.getMessage(), 255));
                if (event.getRetryCount() >= event.getMaxRetries()) {
                    event.setStatus(Status.FAILED);
                    log.error("Event max retries exhausted, marking FAILED: eventId={}", event.getEventId());
                }
            }
        }
    }

    private void deliver(OutboxEvent event) {
        log.info("Delivering event: type={}, eventId={}", event.getEventType(), event.getEventId());
        // TODO: 后续接入 RabbitMQ，将 event.getPayload() 发到 exchange
    }
}
