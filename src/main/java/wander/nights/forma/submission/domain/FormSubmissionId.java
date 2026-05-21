package wander.nights.forma.submission.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Embeddable;
import wander.nights.forma.shared.identifier.FormId;

import java.util.Objects;

@Schema(name = "表单数据Id（值对象）")
@Embeddable
public record FormSubmissionId(
        FormId formId,
        Integer submissionNo
) {

    public FormSubmissionId {
        Objects.requireNonNull(formId);
        Objects.requireNonNull(submissionNo);
    }
}
