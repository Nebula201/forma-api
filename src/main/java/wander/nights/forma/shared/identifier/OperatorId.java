package wander.nights.forma.shared.identifier;

import java.util.Objects;

/**
 * 操作者Id
 *
 * @param value
 */
public record OperatorId(String value) {

    public OperatorId {
        Objects.requireNonNull(value);
    }

    public static OperatorId of(String value) {
        return new OperatorId(value);
    }
}
