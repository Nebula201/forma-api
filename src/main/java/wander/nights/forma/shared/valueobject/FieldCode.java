package wander.nights.forma.shared.valueobject;


import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 字段编码，单个表单中唯一
 *
 * @param value 编码
 */
public record FieldCode(
        @JsonValue
        String value
) {
    public static FieldCode of(String value) {
        return new FieldCode(value);
    }
}
