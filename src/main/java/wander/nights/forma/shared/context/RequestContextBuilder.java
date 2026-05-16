package wander.nights.forma.shared.context;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import wander.nights.forma.shared.valueobject.UserId;

import java.net.InetAddress;
import java.time.Instant;

@Slf4j
@Component
public class RequestContextBuilder {

    public RequestContext build(HttpServletRequest request) {
        UserAttributes userAttributes = resolveUser(request);
        EnvironmentAttributes envAttributes = resolveEnvironment(request);
        return new RequestContext(userAttributes, envAttributes);
    }

    private UserAttributes resolveUser(HttpServletRequest request) {
        // 优先从 Spring Security 上下文获取
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getPrincipal())) {
            String principalName = authentication.getName();
            return UserAttributes.of(UserId.of(principalName));
        }

        // Fallback: 从请求头获取（网关透传场景）
        String userIdHeader = request.getHeader("X-User-Id");
        if (userIdHeader != null && !userIdHeader.isBlank()) {
            return UserAttributes.of(UserId.of(userIdHeader));
        }

        // 匿名请求
        return UserAttributes.of(null);
    }

    private EnvironmentAttributes resolveEnvironment(HttpServletRequest request) {
        InetAddress ip = resolveIp(request);
        Instant accessedAt = Instant.now();
        String clientType = resolveClientType(request);
        return EnvironmentAttributes.of(ip, accessedAt, clientType);
    }

    private InetAddress resolveIp(HttpServletRequest request) {
        String ip = null;

        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            ip = forwarded.split(",")[0].trim();
        }

        if (ip == null || ip.isBlank()) {
            ip = request.getHeader("X-Real-IP");
        }

        if (ip == null || ip.isBlank()) {
            ip = request.getRemoteAddr();
        }

        try {
            return InetAddress.getByName(ip);
        } catch (Exception e) {
            log.warn("Failed to parse IP: {}", ip, e);
            return null;
        }
    }

    private String resolveClientType(HttpServletRequest request) {
        String clientType = request.getHeader("X-Client-Type");
        return StringUtils.defaultIfBlank(clientType, "unknown");
    }
}
