package wander.nights.forma.model.fields;

import lombok.Data;
import lombok.EqualsAndHashCode;
import wander.nights.forma.model.fields.answer.Answer;
import wander.nights.forma.model.fields.answer.DateFieldAnswer;

import java.time.LocalDate;

/**
 * 日期字段
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DateField extends FieldDefinition {

    private String format = "yyyy-MM-dd";
    private LocalDate minDate;
    private LocalDate maxDate;
    private Boolean enableTime = false;

    @Override
    public <T, A extends FieldDefinition> T accept(FieldVisitor<T> visitor, Answer<A> answerValue) {
        return visitor.visit(this, answerValue);
    }
}