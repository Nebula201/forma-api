package wander.nights.forma.form.command.entity;

import lombok.Data;
import wander.nights.forma.shared.condition.ConditionGroup;
import wander.nights.forma.shared.identifier.FieldCode;

import java.util.Set;

@Data
public class FormAccessPermission {
    // 无限制
    public static final FormAccessPermission UNLIMITED = new FormAccessPermission();

    static {
        UNLIMITED.disallowFields = Set.of();
        UNLIMITED.recordFilters = null;
    }

    /**
     * 禁止访问字段
     */
    private Set<FieldCode> disallowFields;

    /**
     * 记录级权限配置（行级过滤），null表示不限制
     */
    private ConditionGroup recordFilters;
}
