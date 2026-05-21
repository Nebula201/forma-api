package wander.nights.forma.shared.context;

import wander.nights.forma.shared.identifier.OperatorId;

public record UserAttributes(
        OperatorId operatorId
) {
    public static UserAttributes of(OperatorId operatorId) {
        return new UserAttributes(operatorId);
    }
}
