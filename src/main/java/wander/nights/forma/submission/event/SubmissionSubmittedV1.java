package wander.nights.forma.submission.event;

import lombok.Data;
import lombok.NoArgsConstructor;
import wander.nights.forma.submission.domain.FormSubmission;
import wander.nights.forma.event.DomainEvent;
import wander.nights.forma.shared.identifier.FieldCode;
import wander.nights.forma.shared.identifier.FormId;

import java.time.Instant;
import java.util.Map;

@Data
@NoArgsConstructor
public class SubmissionSubmittedV1 implements DomainEvent {
    private FormId formId;
    private Integer submissionNo;
    private Integer formVersion;
    private Map<FieldCode, Object> content;
    private String submittedIp;
    private Instant submittedAt;

    public SubmissionSubmittedV1(FormSubmission formSubmission) {
        this.formId = formSubmission.getFormId();
        this.submissionNo = formSubmission.getFormSubmissionId().submissionNo();
        this.formVersion = formSubmission.getFormVersion();
        this.submittedAt = formSubmission.getSubmittedAt();
        this.content = formSubmission.getContent();
        if (formSubmission.getSubmittedIp() != null) {
            this.submittedIp = formSubmission.getSubmittedIp().getHostAddress();
        }
    }

    @Override
    public String eventType() {
        return "submission.submitted";
    }

    @Override
    public int eventVersion() {
        return 1;
    }
}
