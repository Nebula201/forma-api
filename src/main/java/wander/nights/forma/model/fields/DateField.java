package wander.nights.forma.model.fields;

import lombok.Data;
import lombok.EqualsAndHashCode;

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
}