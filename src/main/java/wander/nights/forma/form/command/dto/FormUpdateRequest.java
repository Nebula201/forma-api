package wander.nights.forma.form.command.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class FormUpdateRequest {
    @Size(max = 255, message = "表单标题长度不能超过255")
    private String title;

    private String description;

    private List<FieldDefinition> fields;

    private List<RuleDefinition> rules;

    private FormSetting setting;

    private String status; // DRAFT, PUBLISHED, CLOSED

    // For using the existing field models
    @Data
    public static class FieldDefinition {
        private String type;
        private String code;
        private String label;
        private String text;
        private Boolean required;
        private Integer position;
    }

    @Data
    public static class RuleDefinition {
        private String condition;
        private List<String> actions;
    }

    @Data
    public static class FormSetting {
        private String string;
    }
}
