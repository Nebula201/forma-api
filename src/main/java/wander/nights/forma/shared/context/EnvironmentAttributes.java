package wander.nights.forma.shared.context;

import java.net.InetAddress;
import java.time.Instant;

public record EnvironmentAttributes(
        InetAddress ip,
        Instant accessedAt,
        String clientType
) {
    public static EnvironmentAttributes of(InetAddress ip, Instant accessedAt, String clientType) {
        return new EnvironmentAttributes(ip, accessedAt, clientType);
    }
}
