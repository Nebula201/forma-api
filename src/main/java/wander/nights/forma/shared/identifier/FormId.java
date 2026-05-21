package wander.nights.forma.shared.identifier;

import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Embeddable;

import java.util.Objects;
import java.util.UUID;

@Embeddable
@Schema(description = "表单Id（值对象）")
public record FormId(
        @JsonValue
        UUID value
) {
    public FormId {
        Objects.requireNonNull(value);
    }

    public static FormId of(String valueStr) {
        return new FormId(UUID.fromString(valueStr));
    }
}
