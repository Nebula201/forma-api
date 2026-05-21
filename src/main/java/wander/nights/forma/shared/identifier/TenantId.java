package wander.nights.forma.shared.identifier;

import java.util.Objects;

/**
 * 租户Id
 *
 * @param value
 */
public record TenantId(String value) {
    public TenantId {
        Objects.requireNonNull(value);
    }
}
