package wander.nights.forma.submission.infrastructure.service;

import wander.nights.forma.model.fields.*;
import wander.nights.forma.model.fields.answer.Answer;
import wander.nights.forma.shared.condition.Operator;

import java.util.*;

public class FilterSqlFieldVisitor implements FieldVisitor<SqlFragment> {

    private static final Map<Operator, String> COMPARISON_OPS = Map.of(
            Operator.EQ, "=",
            Operator.NE, "<>",
            Operator.GE, ">=",
            Operator.GT, ">",
            Operator.LE, "<=",
            Operator.LT, "<"
    );

    private final Operator operator;
    private final Object expectedValue;
    private final String fieldCode;

    public FilterSqlFieldVisitor(Operator operator, Object expectedValue, String fieldCode) {
        this.operator = operator;
        this.expectedValue = expectedValue;
        this.fieldCode = fieldCode;
    }

    @Override
    public <A extends FieldDefinition> SqlFragment visit(TextField field, Answer<A> value) {
        return generateTextSql();
    }

    @Override
    public <A extends FieldDefinition> SqlFragment visit(SelectField field, Answer<A> value) {
        boolean isMulti = "checkbox".equals(field.getType());
        if (isMulti) {
            return generateCheckboxSql();
        }
        return generateSelectSql();
    }

    @Override
    public <A extends FieldDefinition> SqlFragment visit(RatingField field, Answer<A> value) {
        return generateNumericSql();
    }

    @Override
    public <A extends FieldDefinition> SqlFragment visit(DateField field, Answer<A> value) {
        return generateDateSql();
    }

    @Override
    public <A extends FieldDefinition> SqlFragment visit(FileField field, Answer<A> value) {
        return generateTextSql();
    }

    @Override
    public <A extends FieldDefinition> SqlFragment visit(MatrixSelectField field, Answer<A> value) {
        return generateTextSql();
    }

    private String textPath() {
        return "jsonb_extract_path_text(content, '%s')".formatted(fieldCode);
    }

    private SqlFragment generateTextSql() {
        if (expectedValue == null) {
            if (operator == Operator.EMPTY) return new SqlFragment(textPath() + " is null", List.of());
            if (operator == Operator.FILLED) return new SqlFragment(textPath() + " is not null", List.of());
            return new SqlFragment("1=0", List.of());
        }
        return switch (operator) {
            case EQ -> new SqlFragment(textPath() + " = ?", List.of(expectedValue));
            case NE -> new SqlFragment(textPath() + " <> ?", List.of(expectedValue));
            case CONTAINS -> new SqlFragment(textPath() + " like ?", List.of("%" + expectedValue + "%"));
            case NOT_CONTAINS -> new SqlFragment(textPath() + " not like ?", List.of("%" + expectedValue + "%"));
            case START_WITH -> new SqlFragment(textPath() + " like ?", List.of(expectedValue + "%"));
            case END_WITH -> new SqlFragment(textPath() + " like ?", List.of("%" + expectedValue));
            case IN -> generateInSql(textPath());
            case NOT_IN -> generateNotInSql(textPath());
            case FILLED -> new SqlFragment(textPath() + " is not null", List.of());
            case EMPTY -> new SqlFragment(textPath() + " is null", List.of());
            default -> new SqlFragment(textPath() + " " + COMPARISON_OPS.getOrDefault(operator, "=") + " ?", List.of(expectedValue));
        };
    }

    private SqlFragment generateSelectSql() {
        if (expectedValue == null) {
            if (operator == Operator.EMPTY) return new SqlFragment("content->'%s' is null".formatted(fieldCode), List.of());
            if (operator == Operator.FILLED) return new SqlFragment("content->'%s' is not null".formatted(fieldCode), List.of());
            return new SqlFragment("1=0", List.of());
        }
        return switch (operator) {
            case EQ -> new SqlFragment("content->>'%s' = ?".formatted(fieldCode), List.of(expectedValue));
            case NE -> new SqlFragment("content->>'%s' <> ?".formatted(fieldCode), List.of(expectedValue));
            case IN -> generateInSql("content->>'%s'".formatted(fieldCode));
            case NOT_IN -> generateNotInSql("content->>'%s'".formatted(fieldCode));
            case FILLED -> new SqlFragment("content->'%s' is not null".formatted(fieldCode), List.of());
            case EMPTY -> new SqlFragment("content->'%s' is null".formatted(fieldCode), List.of());
            default -> new SqlFragment("content->>'%s' = ?".formatted(fieldCode), List.of(expectedValue));
        };
    }

    private SqlFragment generateCheckboxSql() {
        if (expectedValue == null) {
            if (operator == Operator.EMPTY) return new SqlFragment("content->'%s' is null".formatted(fieldCode), List.of());
            if (operator == Operator.FILLED) return new SqlFragment("content->'%s' is not null".formatted(fieldCode), List.of());
            return new SqlFragment("1=0", List.of());
        }
        return switch (operator) {
            case EQ, CONTAINS -> {
                String jsonVal = "\"%s\"".formatted(expectedValue);
                yield new SqlFragment("content->'%s' @> '%s'::jsonb".formatted(fieldCode, jsonVal), List.of());
            }
            case NE, NOT_CONTAINS -> {
                String jsonVal = "\"%s\"".formatted(expectedValue);
                yield new SqlFragment("not (content->'%s' @> '%s'::jsonb)".formatted(fieldCode, jsonVal), List.of());
            }
            case IN -> {
                if (expectedValue instanceof Collection<?> coll) {
                    List<String> items = coll.stream().map(Object::toString).toList();
                    String jsonArr = items.stream().map(s -> "\"%s\"".formatted(s)).reduce("[", (a, b) -> a.isEmpty() || a.equals("[") ? a + b : a + "," + b) + "]";
                    yield new SqlFragment("content->'%s' @> '%s'::jsonb".formatted(fieldCode, jsonArr), List.of());
                }
                String jsonVal = "\"%s\"".formatted(expectedValue);
                yield new SqlFragment("content->'%s' @> '%s'::jsonb".formatted(fieldCode, jsonVal), List.of());
            }
            case FILLED -> new SqlFragment("content->'%s' is not null".formatted(fieldCode), List.of());
            case EMPTY -> new SqlFragment("content->'%s' is null".formatted(fieldCode), List.of());
            default -> {
                String jsonVal = "\"%s\"".formatted(expectedValue);
                yield new SqlFragment("content->'%s' @> '%s'::jsonb".formatted(fieldCode, jsonVal), List.of());
            }
        };
    }

    private SqlFragment generateNumericSql() {
        String numExpr = "(content->>'%s')::numeric".formatted(fieldCode);
        if (expectedValue == null) {
            if (operator == Operator.EMPTY) return new SqlFragment(numExpr + " is null", List.of());
            if (operator == Operator.FILLED) return new SqlFragment(numExpr + " is not null", List.of());
            return new SqlFragment("1=0", List.of());
        }
        return switch (operator) {
            case EQ -> new SqlFragment(numExpr + " = ?", List.of(expectedValue));
            case NE -> new SqlFragment(numExpr + " <> ?", List.of(expectedValue));
            case GT -> new SqlFragment(numExpr + " > ?", List.of(expectedValue));
            case LT -> new SqlFragment(numExpr + " < ?", List.of(expectedValue));
            case GE -> new SqlFragment(numExpr + " >= ?", List.of(expectedValue));
            case LE -> new SqlFragment(numExpr + " <= ?", List.of(expectedValue));
            case FILLED -> new SqlFragment(numExpr + " is not null", List.of());
            case EMPTY -> new SqlFragment(numExpr + " is null", List.of());
            default -> new SqlFragment(numExpr + " = ?", List.of(expectedValue));
        };
    }

    private SqlFragment generateDateSql() {
        String dateExpr = "(content->>'%s')::date".formatted(fieldCode);
        if (expectedValue == null) {
            if (operator == Operator.EMPTY) return new SqlFragment(dateExpr + " is null", List.of());
            if (operator == Operator.FILLED) return new SqlFragment(dateExpr + " is not null", List.of());
            return new SqlFragment("1=0", List.of());
        }
        return switch (operator) {
            case EQ -> new SqlFragment(dateExpr + " = ?::date", List.of(expectedValue));
            case NE -> new SqlFragment(dateExpr + " <> ?::date", List.of(expectedValue));
            case GT -> new SqlFragment(dateExpr + " > ?::date", List.of(expectedValue));
            case LT -> new SqlFragment(dateExpr + " < ?::date", List.of(expectedValue));
            case GE -> new SqlFragment(dateExpr + " >= ?::date", List.of(expectedValue));
            case LE -> new SqlFragment(dateExpr + " <= ?::date", List.of(expectedValue));
            case FILLED -> new SqlFragment(dateExpr + " is not null", List.of());
            case EMPTY -> new SqlFragment(dateExpr + " is null", List.of());
            default -> new SqlFragment(dateExpr + " = ?::date", List.of(expectedValue));
        };
    }

    private SqlFragment generateInSql(String expr) {
        if (expectedValue instanceof Collection<?> coll) {
            if (coll.isEmpty()) return new SqlFragment("1=0", List.of());
            String placeholders = String.join(",", Collections.nCopies(coll.size(), "?"));
            return new SqlFragment(expr + " in (" + placeholders + ")", new ArrayList<>(coll));
        }
        return new SqlFragment(expr + " = ?", List.of(expectedValue));
    }

    private SqlFragment generateNotInSql(String expr) {
        if (expectedValue instanceof Collection<?> coll) {
            if (coll.isEmpty()) return new SqlFragment("1=1", List.of());
            String placeholders = String.join(",", Collections.nCopies(coll.size(), "?"));
            return new SqlFragment(expr + " not in (" + placeholders + ")", new ArrayList<>(coll));
        }
        return new SqlFragment(expr + " <> ?", List.of(expectedValue));
    }
}
