package wander.nights.forma.event.payload;

import lombok.Data;
import wander.nights.forma.event.DomainEvent;
import wander.nights.forma.shared.valueobject.FormId;
import wander.nights.forma.shared.valueobject.UserId;

import java.time.Instant;

@Data
public class FormDeletedV1 implements DomainEvent {
    private FormId formId;
    private UserId deletedBy;
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
