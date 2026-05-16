package wander.nights.forma.shared.context;

import wander.nights.forma.shared.valueobject.UserId;

public record UserAttributes(
        UserId userId
) {
    public static UserAttributes of(UserId userId) {
        return new UserAttributes(userId);
    }
}
