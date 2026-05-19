package wander.nights.forma.specification;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 条件组（Composite）
 */
@Getter
public class ConditionGroup implements ConditionExpression {
    private final CombineType combineType;
    private final List<ConditionExpression> children;

    public ConditionGroup(CombineType combineType) {
        this(combineType, new ArrayList<>());
    }

    public ConditionGroup(CombineType combineType, List<ConditionExpression> children) {
        this.combineType = combineType;
        this.children = new ArrayList<>(children);
    }

    public ConditionGroup add(ConditionExpression expr) {
        children.add(expr);
        return this;
    }

    public List<ConditionExpression> getChildren() {
        return Collections.unmodifiableList(children);
    }

    @Override
    public <T> T accept(ConditionVisitor<T> visitor) {
        return visitor.visit(this);
    }
}