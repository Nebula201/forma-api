package wander.nights.forma.event.payload;

import lombok.Data;
import lombok.NoArgsConstructor;
import wander.nights.forma.form.command.entity.FormSubmission;
import wander.nights.forma.event.DomainEvent;
import wander.nights.forma.shared.valueobject.FieldCode;
import wander.nights.forma.shared.valueobject.FormId;
import wander.nights.forma.shared.valueobject.FormSubmissionId;

import java.time.Instant;
import java.util.Map;

@Data
@NoArgsConstructor
public class SubmissionSubmittedV1 implements DomainEvent {
    private FormId formId;
    private Integer formVersion;
    private FormSubmissionId submissionId;
    private Map<FieldCode, Object> content;
    private String submittedIp;
    private Instant submittedAt;

    public SubmissionSubmittedV1(FormSubmission formSubmission) {
        this.formId = formSubmission.getFormId();
        this.formVersion = formSubmission.getFormVersion();
        this.submissionId = formSubmission.getFormSubmissionId();
        this.submittedAt = formSubmission.getSubmittedAt();
        this.submittedIp = formSubmission.getSubmittedIp().getHostAddress();
        this.content = formSubmission.getContent();
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
