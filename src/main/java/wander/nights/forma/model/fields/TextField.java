package wander.nights.forma.model.fields;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 文本类字段
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TextField extends FieldDefinition {
    private String placeholder;
    private String defaultValue;
    private Integer rows = 1;  // textarea 专用
    private Boolean autoComplete = true;

    private Integer maxLength;
}