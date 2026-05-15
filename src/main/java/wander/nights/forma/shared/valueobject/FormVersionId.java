package wander.nights.forma.shared.valueobject;

import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.UUID;

@Embeddable
public record FormVersionId(
        @JsonValue
        @Column(name = "form_version_id")
        UUID value
) {
}
