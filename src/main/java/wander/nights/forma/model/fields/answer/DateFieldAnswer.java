package wander.nights.forma.model.fields.answer;

import lombok.Data;
import wander.nights.forma.model.fields.DateField;

@Data
public class DateFieldAnswer implements Answer<DateField> {
    private String date;

    @Override
    public Object rawValue() {
        return date;
    }
}
