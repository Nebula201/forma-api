package wander.nights.forma.model.fields;

import lombok.Data;
import lombok.EqualsAndHashCode;
import wander.nights.forma.model.fields.answer.Answer;

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

    @Override
    public <T, F extends FieldDefinition> T accept(FieldVisitor<T> visitor, Answer<F> answerValue) {
        return visitor.visit(this, answerValue);
    }
}