package wander.nights.forma.model.fields.answer;

import lombok.Data;
import wander.nights.forma.model.fields.FileField;

@Data
public class FileFieldAnswer implements Answer<FileField> {
    private String url;

    @Override
    public Object rawValue() {
        return url;
    }
}
