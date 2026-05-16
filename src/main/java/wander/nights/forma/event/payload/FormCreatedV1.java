package wander.nights.forma.event.payload;

import lombok.Data;
import wander.nights.forma.event.DomainEvent;

import java.time.Instant;

@Data
public class FormCreatedV1 implements DomainEvent {
    private String formId;
    private String formCode;
    private String formName;
    private String formDescription;
    private String createdBy;
    private Instant createdAt;

    @Override
    public String eventType() {
        return "form.created";
    }

    @Override
    public int eventVersion() {
        return 1;
    }
}
