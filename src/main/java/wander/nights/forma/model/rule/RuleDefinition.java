package wander.nights.forma.model.rule;

import lombok.Data;
import wander.nights.forma.shared.identifier.FieldCode;

import java.util.Set;

@Data
public class RuleDefinition {

    /**
     * 条件组合方式：全部/任一
     */
    private Condition.Type conditionType = Condition.Type.ALL;
    /**
     * 条件
     */
    private Set<Condition> conditions;

    /**
     * 满足条件时显示的字段编码列表
     */
    private Set<FieldCode> showFields;

    /**
     * 满足条件时隐藏的字段编码列表
     */
    private Set<FieldCode> hideFields;
}
