package wander.nights.forma.model.fields.answer;

import lombok.Data;
import wander.nights.forma.model.fields.MatrixSelectField;

import java.util.Map;

@Data
public class MatrixSelectFieldAnswer implements Answer<MatrixSelectField> {
    private Map<String, String> answers;

    @Override
    public Object rawValue() {
        return answers;
    }
}
