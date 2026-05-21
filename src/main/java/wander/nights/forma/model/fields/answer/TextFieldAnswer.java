package wander.nights.forma.model.fields.answer;

import lombok.Data;
import wander.nights.forma.model.fields.TextField;

@Data
public class TextFieldAnswer implements Answer<TextField> {
    private String answer;

    @Override
    public Object rawValue() {
        return answer;
    }
}
