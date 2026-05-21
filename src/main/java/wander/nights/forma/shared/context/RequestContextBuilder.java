package wander.nights.forma.shared.context;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import wander.nights.forma.shared.service.IpService;
import wander.nights.forma.shared.valueobject.Region;
import wander.nights.forma.shared.identifier.OperatorId;

import java.net.InetAddress;
import java.time.Instant;

@Slf4j
@Component
@RequiredArgsConstructor
public class RequestContextBuilder {
    private static final String UNKNOWN = "unknown";
    private static final String LOOPBACK_ADDRESS = "0:0:0:0:0:0:0:1";
    private static final String LOCALHOST = "127.0.0.1";

    private final IpService ipService;

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
            return UserAttributes.of(OperatorId.of(principalName));
        }

        // Fallback: 从请求头获取（网关透传场景）
        String userIdHeader = request.getHeader("X-User-Id");
        if (userIdHeader != null && !userIdHeader.isBlank()) {
            return UserAttributes.of(OperatorId.of(userIdHeader));
        }

        // 匿名请求
        return UserAttributes.of(null);
    }

    private EnvironmentAttributes resolveEnvironment(HttpServletRequest request) {
        InetAddress ip = resolveIp(request);
        Instant accessedAt = Instant.now();
        String userAgent = resolveUserAgent(request);
        String deviceType = resolveDeviceType(userAgent);
        String os = resolveOperateSystem(userAgent);
        String deviceHash = resolveDeviceHash(request);
        Region region = resolveRegion(ip);
        return new EnvironmentAttributes(ip, accessedAt, deviceType, deviceHash, region, userAgent, os);
    }

    private InetAddress resolveIp(HttpServletRequest request) {
        String[] headers = {
                "X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_X_FORWARDED_FOR",
                "HTTP_X_FORWARDED", "HTTP_X_CLUSTER_CLIENT_IP", "HTTP_CLIENT_IP",
                "HTTP_FORWARDED_FOR", "HTTP_FORWARDED", "HTTP_VIA", "REMOTE_ADDR"
        };

        String ip = null;
        for (String header : headers) {
            ip = request.getHeader(header);
            if (StringUtils.isNotBlank(ip) && !UNKNOWN.equalsIgnoreCase(ip)) {
                break;
            }
        }

        if (!StringUtils.isNotBlank(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        // 如果IP地址是IPv6的本地地址，转换为IPv4的127.0.0.1
        if (LOOPBACK_ADDRESS.equals(ip)) {
            ip = LOCALHOST;
        }

        // 如果IP地址包含多个代理服务器地址，取第一个非unknown的IP地址
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0];
        }

        try {
            return InetAddress.getByName(ip);
        } catch (Exception e) {
            log.warn("Failed to parse IP: {}", ip, e);
            return null;
        }
    }

    private String resolveDeviceType(String userAgent) {
        if (userAgent == null) {
            return "未知设备";
        }

        // 判断平板
        if (userAgent.matches("(?i).*(tablet|ipad|playbook|silk)|(android(?!.*mobi)).*")) {
            return "平板电脑";
        }

        // 判断手机
        if (userAgent.matches("(?i).*(Mobile|iP(hone|od)|Android|BlackBerry|IEMobile|Kindle|Silk-Accelerated|(hpw|web)OS|Opera M(obi|ini)).*")) {
            return "手机";
        }

        // 默认桌面
        return "桌面设备";
    }

    private String resolveDeviceHash(HttpServletRequest request) {
        return request.getHeader("X-Device-Hash");
    }


    private Region resolveRegion(InetAddress ip) {
        if (ip == null) return Region.UNKNOWN;
        return ipService.resolveIpAddress(ip);
    }

    private String resolveUserAgent(HttpServletRequest request) {
        return StringUtils.defaultString(request.getHeader("User-Agent"));
    }

    private String resolveOperateSystem(String userAgent) {
        if (userAgent == null || userAgent.isEmpty()) {
            return "unknown";
        }

        userAgent = userAgent.toLowerCase();

        if (userAgent.contains("windows nt 10.0")) {
            return "Windows 10";
        } else if (userAgent.contains("windows nt 6.3")) {
            return "Windows 8.1";
        } else if (userAgent.contains("windows nt 6.2")) {
            return "Windows 8";
        } else if (userAgent.contains("windows nt 6.1")) {
            return "Windows 7";
        } else if (userAgent.contains("windows nt 6.0")) {
            return "Windows Vista";
        } else if (userAgent.contains("windows nt 5.1") || userAgent.contains("windows xp")) {
            return "Windows XP";
        } else if (userAgent.contains("windows nt 5.0")) {
            return "Windows 2000";
        } else if (userAgent.contains("macintosh") || userAgent.contains("mac os x")) {
            return "Mac OS X";
        } else if (userAgent.contains("android")) {
            return "Android";
        } else if (userAgent.contains("iphone") || userAgent.contains("ipad") || userAgent.contains("ipod")) {
            return "iOS";
        } else if (userAgent.contains("linux")) {
            return "Linux";
        } else {
            return "unknown";
        }
    }
}
