package wander.nights.forma.shared.identifier;


import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Objects;

/**
 * 字段编码，单个表单中唯一
 *
 * @param value 编码
 */
public record FieldCode(
        @JsonValue
        String value
) {
    public FieldCode {
        Objects.requireNonNull(value);
    }

    public static FieldCode of(String value) {
        return new FieldCode(value);
    }
}
