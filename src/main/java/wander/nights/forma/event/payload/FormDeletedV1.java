package wander.nights.forma.event.payload;

import lombok.Data;
import wander.nights.forma.event.DomainEvent;
import wander.nights.forma.shared.identifier.FormId;
import wander.nights.forma.shared.identifier.OperatorId;

import java.time.Instant;

@Data
public class FormDeletedV1 implements DomainEvent {
    private FormId formId;
    private OperatorId deletedBy;
    private Instant deletedAt;

    @Override
    public String eventType() {
        return "form.deleted";
    }

    @Override
    public int eventVersion() {
        return 1;
    }
}
