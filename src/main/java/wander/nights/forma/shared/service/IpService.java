package wander.nights.forma.shared.service;

import wander.nights.forma.shared.valueobject.Region;

import java.net.InetAddress;

/**
 * IP地理位置解析服务接口。
 *
 * <p>该接口定义了将IP地址解析为地理位置信息（国家、省份、城市、运营商等）的标准规范。
 * 不同的实现类可以基于不同的数据源（如本地IP库、在线API、商业化服务等）提供解析能力。
 *
 * <h3>实现注意事项：</h3>
 * <ul>
 *   <li>实现类应当是无状态的，支持并发调用</li>
 *   <li>解析失败时应返回 {@link Region#UNKNOWN}，而非抛出异常</li>
 *   <li>内网/保留地址应返回 {@link Region#PRIVATE}</li>
 *   <li>实现类应当支持优雅降级，避免因单一服务故障影响业务</li>
 * </ul>
 *
 * <h3>使用示例：</h3>
 * <pre>{@code
 * IpService ipService = new IP2RegionIpService("/path/to/ip2region.xdb");
 * Region region = ipService.resolveIpAddress(InetAddress.getByName("119.75.217.56"));
 * if (region.isValid()) {
 *     System.out.println(region.country() + region.province() + region.city());
 * }
 * }</pre>
 *
 * @see Region
 * @see Region#UNKNOWN
 * @see Region#PRIVATE
 */
public interface IpService {

    /**
     * 解析IP地址的实际地理位置信息。
     *
     * <p>该方法根据传入的IP地址，返回对应的地理位置信息，包括国家、省份、城市、
     * 国家代码和运营商（ISP）。对于不同场景的IP地址，返回值遵循以下约定：
     *
     * <ul>
     *   <li><b>公网IP且解析成功</b>：返回包含完整地理信息的 {@link Region} 对象</li>
     *   <li><b>内网IP/保留地址</b>：返回 {@link Region#PRIVATE} 常量</li>
     *   <li><b>公网IP但解析失败</b>（数据源无记录）：返回 {@link Region#UNKNOWN} 常量</li>
     *   <li><b>参数为null</b>：返回 {@link Region#UNKNOWN} 常量</li>
     * </ul>
     *
     * <p>实现类应当保证该方法是线程安全的，并且具备合理的性能表现。
     * 对于离线IP库实现，建议初始化时加载数据到内存以提升查询速度。
     *
     * @param host 待解析的IP地址，可为null
     * @return 解析后的地理位置信息，永不返回null
     * <ul>
     *   <li>解析成功：包含有效数据的Region实例</li>
     *   <li>内网IP：{@link Region#PRIVATE}</li>
     *   <li>解析失败或参数为null：{@link Region#UNKNOWN}</li>
     * </ul>
     */
    Region resolveIpAddress(String host);

    default Region resolveIpAddress(InetAddress inetAddress) {
        if (inetAddress == null) return Region.UNKNOWN;
        return resolveIpAddress(inetAddress.getHostAddress());
    }


}
