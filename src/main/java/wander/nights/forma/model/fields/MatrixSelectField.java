package wander.nights.forma.model.fields;

import wander.nights.forma.model.fields.answer.Answer;

/**
 * 矩阵选择
 */
public class MatrixSelectField extends FieldDefinition {
    @Override
    public <T, F extends FieldDefinition> T accept(FieldVisitor<T> visitor, Answer<F> answerValue) {
        return visitor.visit(this, answerValue);
    }
}
