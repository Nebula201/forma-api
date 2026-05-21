package wander.nights.forma.model.fields;

import lombok.Data;
import lombok.EqualsAndHashCode;
import wander.nights.forma.model.fields.answer.Answer;

import java.util.List;

/**
 * 选择类字段
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SelectField extends FieldDefinition {

    private List<Option> options;
    private Boolean allowOther = false;     // 允许填写"其他"
    private Integer maxSelect;              // 多选时最多可选数量
    private String otherPlaceholder = "请输入";


    @Data
    public static class Option {
        private String value;
        private String label;
        private Boolean disabled = false;
    }

    @Override
    public <T, F extends FieldDefinition> T accept(FieldVisitor<T> visitor, Answer<F> answerValue) {
        return visitor.visit(this, answerValue);
    }
}