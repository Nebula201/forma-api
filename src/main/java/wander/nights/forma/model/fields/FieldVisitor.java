package wander.nights.forma.model.fields;

import wander.nights.forma.model.fields.answer.*;

public interface FieldVisitor<T> {

    <A extends FieldDefinition> T visit(DateField field, Answer<A> value);

    <A extends FieldDefinition> T visit(TextField field, Answer<A> value);

    <A extends FieldDefinition> T visit(RatingField field, Answer<A> value);

    <A extends FieldDefinition> T visit(FileField field, Answer<A> value);

    <A extends FieldDefinition> T visit(SelectField field, Answer<A> value);

    <A extends FieldDefinition> T visit(MatrixSelectField field, Answer<A> value);
}
