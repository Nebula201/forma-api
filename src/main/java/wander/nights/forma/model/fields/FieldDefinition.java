package wander.nights.forma.model.fields;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import lombok.NoArgsConstructor;
import wander.nights.forma.model.fields.answer.Answer;
import wander.nights.forma.shared.identifier.FieldCode;

/**
 * 字段定义基类，支持多态反序列化
 */
@Data
@NoArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = TextField.class, name = "text"),
        @JsonSubTypes.Type(value = TextField.class, name = "textarea"),
        @JsonSubTypes.Type(value = TextField.class, name = "email"),
        @JsonSubTypes.Type(value = TextField.class, name = "phone"),
        @JsonSubTypes.Type(value = SelectField.class, name = "radio"),
        @JsonSubTypes.Type(value = SelectField.class, name = "checkbox"),
        @JsonSubTypes.Type(value = SelectField.class, name = "select"),
        @JsonSubTypes.Type(value = RatingField.class, name = "rating"),
        @JsonSubTypes.Type(value = DateField.class, name = "date"),
        @JsonSubTypes.Type(value = DateField.class, name = "time"),
        @JsonSubTypes.Type(value = FileField.class, name = "file"),
        @JsonSubTypes.Type(value = MatrixSelectField.class, name = "matrix")
})
public abstract class FieldDefinition {
    /**
     * 字段类型
     */
    private String type;

    /**
     * 字段编码，表单内唯一
     */
    private FieldCode code;

    /**
     * 字段名称
     */
    private String label;

    /**
     * 描述
     */
    private String text;

    /**
     * 是否必填
     */
    private Boolean required = false;

    /**
     * 排序
     */
    private Integer position;

    public abstract <T, F extends FieldDefinition> T accept(FieldVisitor<T> visitor, Answer<F> answerValue);
}
