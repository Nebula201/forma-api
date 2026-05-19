package wander.nights.forma.form.query.service.impl;

import wander.nights.forma.specification.*;

import java.util.*;

/**
 * @param allowedFields 字段名白名单（可由外部注入）
 */
public record SqlGenerateVisitor(Set<String> allowedFields) implements ConditionVisitor<SqlFragment> {

    @Override
    public SqlFragment visit(Condition condition) {
        String field = condition.field().value();
        if (!allowedFields.contains(field)) {
            throw new IllegalArgumentException("Field '" + field + "' is not allowed in SQL generation");
        }
        Operator op = condition.operator();
        Object value = condition.expectedValue();
        String escapedField = "jsonb_extract_path_text(content, '%s')".formatted(field);  // MySQL 转义

        // 处理 NULL 情况
        if (value == null) {
            if (op == Operator.EMPTY) return new SqlFragment(escapedField + " IS NULL", Collections.emptyList());
            if (op == Operator.FILLED) return new SqlFragment(escapedField + " IS NOT NULL", Collections.emptyList());
            return new SqlFragment("1=0", Collections.emptyList()); // 其他操作符与 NULL 无意义
        }
        switch (op) {
            case Operator.CONTAINS:
                return new SqlFragment(escapedField + " LIKE ?", Collections.singletonList("%" + value + "%"));
            case Operator.START_WITH:
                return new SqlFragment(escapedField + " LIKE ?", Collections.singletonList(value + "%"));
            case Operator.END_WITH:
                return new SqlFragment(escapedField + " LIKE ?", Collections.singletonList("%" + value));
            case Operator.IN:
                if (value instanceof Collection<?> collection) {
                    if (collection.isEmpty()) {
                        return new SqlFragment("1=0", Collections.emptyList());
                    }
                    String placeholders = String.join(",", Collections.nCopies(collection.size(), "?"));
                    String sql = escapedField + " in (" + placeholders + ") ";
                    return new SqlFragment(sql, new ArrayList<>(collection));
                } else {
                    // 非集合降级为 =
                    return new SqlFragment(escapedField + " = ?", Collections.singletonList(value));
                }
            case Operator.NOT_IN:
                if (value instanceof Collection<?> collection) {
                    if (collection.isEmpty()) {
                        return new SqlFragment("1=1", Collections.emptyList());
                    }
                    String placeholders = String.join(",", Collections.nCopies(collection.size(), "?"));
                    String sql = escapedField + " not in (" + placeholders + ") ";
                    return new SqlFragment(sql, new ArrayList<>(collection));
                } else {
                    // 非集合降级为 <>
                    return new SqlFragment(escapedField + " <> ?", Collections.singletonList(value));
                }
                // TODO 所有操作都要枚举
            default:
                return new SqlFragment(escapedField + " " + op + " ?", Collections.singletonList(value));
        }
    }

    @Override
    public SqlFragment visit(ConditionGroup group) {
        if (group.getChildren().isEmpty()) return SqlFragment.empty();

        List<SqlFragment> childFragments = new ArrayList<>();
        for (ConditionExpression child : group.getChildren()) {
            SqlFragment frag = child.accept(this);
            if (frag != null && !frag.sql().isEmpty()) {
                childFragments.add(frag);
            }
        }
        if (childFragments.isEmpty()) return SqlFragment.empty();

        if (group.getCombineType() == CombineType.ALL) {
            return SqlFragment.and(childFragments);
        } else {
            return SqlFragment.or(childFragments);
        }
    }

}
