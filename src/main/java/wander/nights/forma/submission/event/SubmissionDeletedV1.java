package wander.nights.forma.submission.event;

import lombok.Data;
import wander.nights.forma.event.DomainEvent;
import wander.nights.forma.shared.identifier.FormId;
import wander.nights.forma.shared.identifier.OperatorId;

import java.time.Instant;

@Data
public class SubmissionDeletedV1 implements DomainEvent {
    private FormId formId;
    private Integer submissionNo;
    private OperatorId deletedBy;
    private Instant deletedAt;

    @Override
    public String eventType() {
        return "submission.deleted";
    }

    @Override
    public int eventVersion() {
        return 1;
    }
}
