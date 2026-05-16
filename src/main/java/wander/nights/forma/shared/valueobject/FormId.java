package wander.nights.forma.shared.valueobject;

import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.UUID;

@Embeddable
@Schema(description = "表单Id（值对象）")
public record FormId(
        @JsonValue
        UUID value
) {
    public static FormId of(String valueStr) {
        return new FormId(UUID.fromString(valueStr));
    }
}
