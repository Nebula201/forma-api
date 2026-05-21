package wander.nights.forma.form.query.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import wander.nights.forma.model.fields.*;
import wander.nights.forma.shared.condition.*;
import wander.nights.forma.shared.identifier.FieldCode;
import wander.nights.forma.shared.identifier.FormId;
import wander.nights.forma.submission.infrastructure.service.FilterSqlFieldVisitor;
import wander.nights.forma.submission.infrastructure.service.SqlFragment;
import wander.nights.forma.submission.infrastructure.service.SqlGenerateVisitor;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SqlGenerateVisitorTest {

    private final Set<String> allowedFields = Set.of("name", "city", "age", "satisfaction", "birth_date", "hobbies", "gender", "attachment");

    @Test
    void genericTextFallback() {
        ConditionExpression expr = new ConditionGroup(CombineType.ANY)
                .add(new ConditionGroup(CombineType.ALL)
                        .add(new Condition(new FieldCode("age"), Operator.GT, 18))
                        .add(new ConditionGroup(CombineType.ANY)
                                .add(new Condition(new FieldCode("city"), Operator.EQ, "北京"))
                                .add(new Condition(new FieldCode("city"), Operator.EQ, "上海"))))
                .add(new Condition(new FieldCode("name"), Operator.START_WITH, "张"));

        SqlGenerateVisitor visitor = new SqlGenerateVisitor(allowedFields);
        SqlFragment fragment = expr.accept(visitor);
        System.out.println("SQL WHERE: " + fragment.sql());
        System.out.println("Params: " + fragment.params());
        assertFalse(fragment.sql().isEmpty());
    }

    @Nested
    @DisplayName("FilterSqlFieldVisitor - TextField")
    class TextFieldTest {
        private final TextField textField = createTextField("name", "text");

        @Test
        void eq() {
            SqlFragment frag = textField.accept(new FilterSqlFieldVisitor(Operator.EQ, "张三", "name"), null);
            assertEquals("jsonb_extract_path_text(content, 'name') = ?", frag.sql());
            assertEquals(List.of("张三"), frag.params());
        }

        @Test
        void contains() {
            SqlFragment frag = textField.accept(new FilterSqlFieldVisitor(Operator.CONTAINS, "张", "name"), null);
            assertEquals("jsonb_extract_path_text(content, 'name') like ?", frag.sql());
            assertEquals(List.of("%张%"), frag.params());
        }

        @Test
        void empty() {
            SqlFragment frag = textField.accept(new FilterSqlFieldVisitor(Operator.EMPTY, null, "name"), null);
            assertEquals("jsonb_extract_path_text(content, 'name') is null", frag.sql());
            assertTrue(frag.params().isEmpty());
        }
    }

    @Nested
    @DisplayName("FilterSqlFieldVisitor - SelectField (radio)")
    class SelectFieldRadioTest {
        private final SelectField radioField = createSelectField("gender", "radio");

        @Test
        void eq() {
            SqlFragment frag = radioField.accept(new FilterSqlFieldVisitor(Operator.EQ, "男", "gender"), null);
            assertEquals("content->>'gender' = ?", frag.sql());
            assertEquals(List.of("男"), frag.params());
        }

        @Test
        void in() {
            SqlFragment frag = radioField.accept(new FilterSqlFieldVisitor(Operator.IN, List.of("男", "女"), "gender"), null);
            assertTrue(frag.sql().contains("in (?,"), frag.sql());
            assertEquals(List.of("男", "女"), frag.params());
        }
    }

    @Nested
    @DisplayName("FilterSqlFieldVisitor - SelectField (checkbox)")
    class SelectFieldCheckboxTest {
        private final SelectField checkboxField = createSelectField("hobbies", "checkbox");

        @Test
        void contains() {
            SqlFragment frag = checkboxField.accept(new FilterSqlFieldVisitor(Operator.CONTAINS, "阅读", "hobbies"), null);
            assertEquals("content->'hobbies' @> '\"阅读\"'::jsonb", frag.sql());
            assertTrue(frag.params().isEmpty());
        }

        @Test
        void notContains() {
            SqlFragment frag = checkboxField.accept(new FilterSqlFieldVisitor(Operator.NOT_CONTAINS, "游戏", "hobbies"), null);
            assertEquals("not (content->'hobbies' @> '\"游戏\"'::jsonb)", frag.sql());
        }

        @Test
        void inMultipleValues() {
            SqlFragment frag = checkboxField.accept(new FilterSqlFieldVisitor(Operator.IN, List.of("阅读", "音乐"), "hobbies"), null);
            assertTrue(frag.sql().contains("@>"));
            assertTrue(frag.sql().contains("[\"阅读\",\"音乐\"]"));
        }
    }

    @Nested
    @DisplayName("FilterSqlFieldVisitor - RatingField")
    class RatingFieldTest {
        private final RatingField ratingField = createRatingField("satisfaction");

        @Test
        void gt() {
            SqlFragment frag = ratingField.accept(new FilterSqlFieldVisitor(Operator.GT, 3, "satisfaction"), null);
            assertEquals("(content->>'satisfaction')::numeric > ?", frag.sql());
            assertEquals(List.of(3), frag.params());
        }

        @Test
        void le() {
            SqlFragment frag = ratingField.accept(new FilterSqlFieldVisitor(Operator.LE, 2, "satisfaction"), null);
            assertEquals("(content->>'satisfaction')::numeric <= ?", frag.sql());
        }
    }

    @Nested
    @DisplayName("FilterSqlFieldVisitor - DateField")
    class DateFieldTest {
        private final DateField dateField = createDateField("birth_date");

        @Test
        void gt() {
            SqlFragment frag = dateField.accept(new FilterSqlFieldVisitor(Operator.GT, "2000-01-01", "birth_date"), null);
            assertEquals("(content->>'birth_date')::date > ?::date", frag.sql());
            assertEquals(List.of("2000-01-01"), frag.params());
        }

        @Test
        void eq() {
            SqlFragment frag = dateField.accept(new FilterSqlFieldVisitor(Operator.EQ, "2025-05-21", "birth_date"), null);
            assertEquals("(content->>'birth_date')::date = ?::date", frag.sql());
        }
    }

    @Nested
    @DisplayName("FilterSqlFieldVisitor - FileField")
    class FileFieldTest {
        private final FileField fileField = createFileField("attachment");

        @Test
        void contains() {
            SqlFragment frag = fileField.accept(new FilterSqlFieldVisitor(Operator.CONTAINS, "pdf", "attachment"), null);
            assertEquals("jsonb_extract_path_text(content, 'attachment') like ?", frag.sql());
            assertEquals(List.of("%pdf%"), frag.params());
        }
    }

    @Nested
    @DisplayName("SqlGenerateVisitor with fieldDefinitions")
    class TypedVisitorTest {
        @Test
        void withFieldDefinitions_ratingField() {
            RatingField ratingField = createRatingField("satisfaction");
            Map<FieldCode, FieldDefinition> fieldDefs = Map.of(new FieldCode("satisfaction"), ratingField);
            Set<String> allowed = Set.of("satisfaction");

            SqlGenerateVisitor visitor = new SqlGenerateVisitor(allowed, fieldDefs);
            SqlFragment frag = visitor.visit(new Condition(new FieldCode("satisfaction"), Operator.GT, 4));

            assertEquals("(content->>'satisfaction')::numeric > ?", frag.sql());
            assertEquals(List.of(4), frag.params());
        }

        @Test
        void withFieldDefinitions_checkboxField() {
            SelectField checkbox = createSelectField("hobbies", "checkbox");
            Map<FieldCode, FieldDefinition> fieldDefs = Map.of(new FieldCode("hobbies"), checkbox);
            Set<String> allowed = Set.of("hobbies");

            SqlGenerateVisitor visitor = new SqlGenerateVisitor(allowed, fieldDefs);
            SqlFragment frag = visitor.visit(new Condition(new FieldCode("hobbies"), Operator.CONTAINS, "编程"));

            assertEquals("content->'hobbies' @> '\"编程\"'::jsonb", frag.sql());
        }

        @Test
        void unknownField_fallsBackToGenericText() {
            Map<FieldCode, FieldDefinition> fieldDefs = Map.of();
            Set<String> allowed = Set.of("unknown_field");

            SqlGenerateVisitor visitor = new SqlGenerateVisitor(allowed, fieldDefs);
            SqlFragment frag = visitor.visit(new Condition(new FieldCode("unknown_field"), Operator.EQ, "test"));

            assertEquals("jsonb_extract_path_text(content, 'unknown_field') = ?", frag.sql());
        }

        @Test
        void conditionGroup_mixedTypes() {
            TextField nameField = createTextField("name", "text");
            RatingField ratingField = createRatingField("score");
            SelectField checkboxField = createSelectField("tags", "checkbox");

            Map<FieldCode, FieldDefinition> fieldDefs = Map.of(
                    new FieldCode("name"), nameField,
                    new FieldCode("score"), ratingField,
                    new FieldCode("tags"), checkboxField
            );
            Set<String> allowed = Set.of("name", "score", "tags");

            ConditionExpression expr = new ConditionGroup(CombineType.ALL)
                    .add(new Condition(new FieldCode("name"), Operator.CONTAINS, "张"))
                    .add(new Condition(new FieldCode("score"), Operator.GE, 4))
                    .add(new Condition(new FieldCode("tags"), Operator.CONTAINS, "技术"));

            SqlGenerateVisitor visitor = new SqlGenerateVisitor(allowed, fieldDefs);
            SqlFragment frag = expr.accept(visitor);

            String sql = frag.sql();
            assertTrue(sql.contains("jsonb_extract_path_text(content, 'name') like ?"));
            assertTrue(sql.contains("(content->>'score')::numeric >= ?"));
            assertTrue(sql.contains("content->'tags' @> '\"技术\"'::jsonb"));
        }
    }

    // --- Helper factories ---

    private TextField createTextField(String code, String type) {
        TextField f = new TextField();
        f.setType(type);
        f.setCode(new FieldCode(code));
        return f;
    }

    private SelectField createSelectField(String code, String type) {
        SelectField f = new SelectField();
        f.setType(type);
        f.setCode(new FieldCode(code));
        return f;
    }

    private RatingField createRatingField(String code) {
        RatingField f = new RatingField();
        f.setType("rating");
        f.setCode(new FieldCode(code));
        return f;
    }

    private DateField createDateField(String code) {
        DateField f = new DateField();
        f.setType("date");
        f.setCode(new FieldCode(code));
        return f;
    }

    private FileField createFileField(String code) {
        FileField f = new FileField();
        f.setType("file");
        f.setCode(new FieldCode(code));
        return f;
    }
}
