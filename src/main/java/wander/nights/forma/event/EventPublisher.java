package wander.nights.forma.event;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import wander.nights.forma.event.outbox.OutboxEvent;
import wander.nights.forma.event.outbox.OutboxEventRepository;

@Component
@RequiredArgsConstructor
public class EventPublisher {

    private final OutboxEventRepository outboxEventRepository;

    public void publish(FormaEvent<? extends DomainEvent> event) {
        String payload = Events.serialize(event);

        OutboxEvent outboxEvent = new OutboxEvent();
        outboxEvent.setEventId(event.getEventId());
        outboxEvent.setEventType(event.getEventType());
        outboxEvent.setEventVersion(event.getEventVersion());
        outboxEvent.setEventAt(event.getEventAt());
        outboxEvent.setPayload(payload);
        outboxEvent.setStatus(OutboxEvent.Status.PENDING);

        outboxEventRepository.save(outboxEvent);
    }
}
