package wander.nights.forma.specification;

/**
 * 条件表达式接口（组合模式 Component）
 */
public interface ConditionExpression {
    <T> T accept(ConditionVisitor<T> visitor);
}
