package wander.nights.forma.submission.infrastructure.service;

import lombok.extern.slf4j.Slf4j;
import wander.nights.forma.model.fields.FieldDefinition;
import wander.nights.forma.shared.condition.*;
import wander.nights.forma.shared.identifier.FieldCode;

import java.util.*;

@Slf4j
public record SqlGenerateVisitor(
        Set<String> allowedFields,
        Map<FieldCode, FieldDefinition> fieldDefinitions
) implements ConditionVisitor<SqlFragment> {

    public SqlGenerateVisitor(Set<String> allowedFields) {
        this(allowedFields, Map.of());
    }

    @Override
    public SqlFragment visit(Condition condition) {
        String field = condition.field().value();
        if (!allowedFields.contains(field) || !fieldDefinitions.containsKey(condition.field())) {
            throw new IllegalArgumentException("Field '" + field + "' is not allowed in SQL generation");
        }

        FieldDefinition fieldDef = fieldDefinitions.get(condition.field());
        if (fieldDef != null) {
            FilterSqlFieldVisitor fieldVisitor = new FilterSqlFieldVisitor(
                    condition.operator(), condition.expectedValue(), field
            );
            return fieldDef.accept(fieldVisitor, null);
        }

        log.warn("No FieldDefinition found for '{}', falling back to generic text SQL", field);
        return generateGenericTextSql(condition);
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

    private static final Map<Operator, String> COMPARISON_OPS = Map.of(
            Operator.EQ, "=",
            Operator.NE, "<>",
            Operator.GE, ">=",
            Operator.GT, ">",
            Operator.LE, "<=",
            Operator.LT, "<"
    );

    private SqlFragment generateGenericTextSql(Condition condition) {
        String field = condition.field().value();
        Operator op = condition.operator();
        Object value = condition.expectedValue();
        String expr = "jsonb_extract_path_text(content, '%s')".formatted(field);

        if (value == null) {
            if (op == Operator.EMPTY) return new SqlFragment(expr + " is null", List.of());
            if (op == Operator.FILLED) return new SqlFragment(expr + " is not null", List.of());
            return new SqlFragment("1=0", List.of());
        }
        return switch (op) {
            case EQ -> new SqlFragment(expr + " = ?", List.of(value));
            case NE -> new SqlFragment(expr + " <> ?", List.of(value));
            case CONTAINS -> new SqlFragment(expr + " like ?", List.of("%" + value + "%"));
            case NOT_CONTAINS -> new SqlFragment(expr + " not like ?", List.of("%" + value + "%"));
            case START_WITH -> new SqlFragment(expr + " like ?", List.of(value + "%"));
            case END_WITH -> new SqlFragment(expr + " like ?", List.of("%" + value));
            case IN -> {
                if (value instanceof Collection<?> coll) {
                    if (coll.isEmpty()) yield new SqlFragment("1=0", List.of());
                    String placeholders = String.join(",", Collections.nCopies(coll.size(), "?"));
                    yield new SqlFragment(expr + " in (" + placeholders + ")", new ArrayList<>(coll));
                }
                yield new SqlFragment(expr + " = ?", List.of(value));
            }
            case NOT_IN -> {
                if (value instanceof Collection<?> coll) {
                    if (coll.isEmpty()) yield new SqlFragment("1=1", List.of());
                    String placeholders = String.join(",", Collections.nCopies(coll.size(), "?"));
                    yield new SqlFragment(expr + " not in (" + placeholders + ")", new ArrayList<>(coll));
                }
                yield new SqlFragment(expr + " <> ?", List.of(value));
            }
            case FILLED -> new SqlFragment(expr + " is not null", List.of());
            case EMPTY -> new SqlFragment(expr + " is null", List.of());
            default -> new SqlFragment(expr + " " + COMPARISON_OPS.getOrDefault(op, "=") + " ?", List.of(value));
        };
    }
}
