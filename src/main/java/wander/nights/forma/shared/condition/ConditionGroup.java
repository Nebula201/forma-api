package wander.nights.forma.shared.condition;

import java.util.ArrayList;
import java.util.List;

/**
 * 条件组（Composite）
 */
public record ConditionGroup(
        CombineType combineType,
        List<ConditionExpression> children
) implements ConditionExpression {
    public ConditionGroup(CombineType combineType) {
        this(combineType, List.of());
    }

    public ConditionGroup(CombineType combineType, List<ConditionExpression> children) {
        this.combineType = combineType;
        this.children = List.copyOf(children);
    }

    public ConditionGroup add(ConditionExpression expr) {
        List<ConditionExpression> newChildren = new ArrayList<>(children);
        newChildren.add(expr);
        return new ConditionGroup(combineType, newChildren);
    }

    @Override
    public <T> T accept(ConditionVisitor<T> visitor) {
        return visitor.visit(this);
    }
}