package wander.nights.forma.model.fields.answer;

import lombok.Data;
import wander.nights.forma.model.fields.RatingField;

@Data
public class RatingFieldAnswer implements Answer<RatingField> {
    private Integer rating;

    @Override
    public Object rawValue() {
        return rating;
    }
}
