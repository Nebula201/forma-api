package wander.nights.forma.shared.context;

import wander.nights.forma.shared.valueobject.Region;

import java.net.InetAddress;
import java.time.Instant;

/**
 * 环境属性
 *
 * @param ip            ip地址
 * @param accessedAt    访问时间
 * @param deviceType    设备类型
 * @param deviceHash    设备指纹
 * @param region        地区
 * @param operateSystem 操作系统
 * @param userAgent     浏览器UA
 */
public record EnvironmentAttributes(
        InetAddress ip,
        Instant accessedAt,
        String deviceType,
        String deviceHash,
        Region region,
        String userAgent,
        String operateSystem
) {
}
