package wander.nights.forma.event.outbox;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import wander.nights.forma.event.outbox.OutboxEvent.Status;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent, UUID> {

    List<OutboxEvent> findByStatusAndEventAtBeforeOrderByEventAtAsc(Status status, Instant before, Pageable pageable);
}
