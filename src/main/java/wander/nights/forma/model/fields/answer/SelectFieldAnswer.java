package wander.nights.forma.model.fields.answer;

import lombok.Data;
import wander.nights.forma.model.fields.SelectField;

import java.util.Map;
@Data
public class SelectFieldAnswer implements Answer<SelectField> {
    private Map<String, String> answer;

    @Override
    public Object rawValue() {
        return answer;
    }
}
