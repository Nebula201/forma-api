package wander.nights.forma.shared.valueobject;

import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.UUID;

@Schema(name = "表单数据Id（值对象）")
@Embeddable
public record FormSubmissionId(
        @JsonValue
        @Column(name = "submission_id")
        UUID value
) {
}
