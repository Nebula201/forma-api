package wander.nights.forma.shared.context;

import wander.nights.forma.shared.valueobject.UserId;

import java.util.Optional;

public final class RequestContext {

    private static final ThreadLocal<RequestContext> HOLDER = new ThreadLocal<>();

    private final UserAttributes userAttributes;
    private final EnvironmentAttributes environmentAttributes;

    public RequestContext(UserAttributes userAttributes, EnvironmentAttributes environmentAttributes) {
        this.userAttributes = userAttributes;
        this.environmentAttributes = environmentAttributes;
    }

    public static void set(RequestContext context) {
        HOLDER.set(context);
    }

    public static RequestContext get() {
        return HOLDER.get();
    }

    public static void clear() {
        HOLDER.remove();
    }

    public static UserAttributes user() {
        RequestContext ctx = HOLDER.get();
        return ctx != null ? ctx.userAttributes : null;
    }

    public static Optional<UserAttributes> userOpt() {
        return Optional.ofNullable(user());
    }

    public static EnvironmentAttributes env() {
        RequestContext ctx = HOLDER.get();
        return ctx != null ? ctx.environmentAttributes : null;
    }

    public static Optional<EnvironmentAttributes> envOpt() {
        return Optional.ofNullable(env());
    }

    public static UserId currentUserId() {
        return userOpt().map(UserAttributes::userId).orElse(null);
    }
}
