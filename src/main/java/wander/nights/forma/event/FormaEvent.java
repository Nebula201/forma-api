package wander.nights.forma.event;

import com.github.f4b6a3.uuid.alt.GUID;
import lombok.Data;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Data
public class FormaEvent<T extends DomainEvent> implements Serializable {
    private UUID eventId = GUID.v7().toUUID();
    private Instant eventAt = Instant.now();
    private String eventType;
    private int eventVersion;
    private String traceId;
    private T payload;

}
