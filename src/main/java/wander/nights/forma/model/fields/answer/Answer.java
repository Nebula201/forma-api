package wander.nights.forma.model.fields.answer;

import wander.nights.forma.model.fields.FieldDefinition;

public interface Answer<F extends FieldDefinition> {

    default Object rawValue() {
        return null;
    }
}
