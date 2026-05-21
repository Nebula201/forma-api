package wander.nights.forma.shared.condition;

import wander.nights.forma.shared.identifier.FieldCode;

import java.util.Objects;

/**
 * 原子条件（Leaf）
 */
public record Condition(
        FieldCode field,
        Operator operator,
        Object expectedValue
) implements ConditionExpression {
    public Condition(FieldCode field, Operator operator, Object expectedValue) {
        this.field = Objects.requireNonNull(field);
        this.operator = Objects.requireNonNull(operator);
        this.expectedValue = expectedValue;
    }

    @Override
    public <T> T accept(ConditionVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
