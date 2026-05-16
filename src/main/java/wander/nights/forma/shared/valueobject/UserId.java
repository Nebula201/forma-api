package wander.nights.forma.shared.valueobject;

import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.Embeddable;

@Embeddable
public record UserId(
        @JsonValue
        String value
) {
    public static UserId of(String valueStr) {
        return new UserId(valueStr);
    }
}
