package wander.nights.forma.shared.condition;

/**
 * 条件组合类型：ALL（所有子条件必须满足，等价于 AND）或 ANY（任一子条件满足，等价于 OR）
 */
public enum CombineType {
    ALL,   // 所有条件必须满足（AND）
    ANY    // 任一条件满足即可（OR）
}
