package wander.nights.forma.shared.condition;

/**
 * 访问者接口（泛型）
 */
public interface ConditionVisitor<T> {
    T visit(Condition condition);

    T visit(ConditionGroup group);
}
