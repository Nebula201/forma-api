package wander.nights.forma.form.query.dto;

import lombok.Data;
import wander.nights.forma.shared.condition.ConditionExpression;

@Data
public class SubmissionQuery {
    ConditionExpression filter;
}
