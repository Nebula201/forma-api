package wander.nights.forma.model;

import lombok.Data;
import wander.nights.forma.model.fields.FieldDefinition;
import wander.nights.forma.model.rule.RuleDefinition;

import java.util.List;

@Data
public class FormContent {
    private List<FieldDefinition> fields;
    private List<RuleDefinition> rules;
}
