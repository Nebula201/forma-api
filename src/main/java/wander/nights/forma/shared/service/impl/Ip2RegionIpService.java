package wander.nights.forma.shared.service.impl;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.service.Config;
import org.lionsoul.ip2region.service.ConfigBuilder;
import org.lionsoul.ip2region.service.InvalidConfigException;
import org.lionsoul.ip2region.service.Ip2Region;
import org.lionsoul.ip2region.xdb.InetAddressException;
import org.lionsoul.ip2region.xdb.XdbException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import wander.nights.forma.shared.service.IpService;
import wander.nights.forma.shared.valueobject.Region;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
@EnableConfigurationProperties(Ip2RegionIpService.Ip2RegionProperties.class)
public class Ip2RegionIpService implements IpService {
    private final ResourceLoader resourceLoader;
    private final Ip2RegionProperties properties;
    private Ip2Region ip2Region;

    @PostConstruct
    public void init() {
        Config v4Config = null, v6Config = null;

        var v4 = properties.getV4();
        if (v4.getEnabled() && v4.getDbPath() != null) {
            v4Config = buildConfig(v4, true);
        }
        var v6 = properties.getV6();
        if (v6.getEnabled() && v6.getDbPath() != null) {
            v6Config = buildConfig(v6, false);
        }

        try {
            // 创建 Ip2Region 查询服务
            ip2Region = Ip2Region.create(v4Config, v6Config);
            log.info("ip解析服务加载: ip2region");
        } catch (IOException e) {
            log.error("ip2region 加载失败", e);
        }

    }

    public String resolveAdapter(String string) throws InetAddressException, IOException, InterruptedException {
        return ip2Region.search(string);
    }

    @PreDestroy
    public void destroy() throws InterruptedException {
        if (ip2Region != null) {
            log.info("ip解析服务卸载: ip2region");
            ip2Region.close();
        }
    }

    @Override
    public Region resolveIpAddress(String host) {
        if (ip2Region == null || host == null || host.isBlank()) return Region.UNKNOWN;

        String searchResult;
        try {
            searchResult = ip2Region.search(host);

            if (searchResult == null || searchResult.isBlank()) {
                return Region.UNKNOWN;
            }
            if ("Reserved|Reserved|Reserved|0|0".equalsIgnoreCase(searchResult)) {
                return Region.PRIVATE;
            }
        } catch (InetAddressException | IOException | InterruptedException e) {
            return Region.UNKNOWN;
        }

        String[] parts = searchResult.split("\\|");
        String country = normalize(parts, 0);
        String province = normalize(parts, 1);
        String city = normalize(parts, 2);
        String isp = normalize(parts, 3);
        String countryCode = normalize(parts, 4);

        return new Region(country, province, city, countryCode, isp);
    }

    private static String normalize(String[] parts, int index) {
        if (index >= parts.length) {
            return Region.UNKNOWN_STR;
        }
        String value = parts[index];
        // ip2region 中 "0" 表示数据缺失
        if (value == null || value.isBlank() || "0".equals(value)) {
            return Region.UNKNOWN_STR;
        }
        return value.trim();
    }

    @Data
    @ConfigurationProperties(prefix = "ip2region")
    public static class Ip2RegionProperties {
        private boolean enable = false;

        private VersionProperties v4 = new VersionProperties();
        private VersionProperties v6 = new VersionProperties();

        @Data
        public static class VersionProperties {
            /**
             * 是否启用
             */
            private Boolean enabled = true;

            /**
             * 数据库文件路径，支持 classpath: 或 file: 前缀
             */
            private String dbPath;

            /**
             * 查询器数量，默认 15
             */
            private Integer searchers = 15;

            /**
             * 缓存策略: NoCache, VIndexCache, BufferCache
             */
            private String cachePolicy = "BufferCache";
        }
    }

    private Config buildConfig(Ip2RegionProperties.VersionProperties props, boolean isV4) {
        try {
            ConfigBuilder builder = Config.custom().setSearchers(props.getSearchers());
            // 设置缓存策略
            builder.setCachePolicy(getCachePolicy(props.getCachePolicy()));
            // 设置数据库文件（优先使用 InputStream，支持 classpath）
            builder.setXdbFile(resourceLoader.getResource(props.getDbPath()).getFile());

            return isV4 ? builder.asV4() : builder.asV6();
        } catch (IOException | InvalidConfigException | XdbException e) {
            log.error("ip2config 配置加载失败! props:[{}]", props, e);
            return null;
        }
    }

    private int getCachePolicy(String policy) {
        if (policy == null) return Config.BufferCache;
        return switch (policy.toLowerCase()) {
            case "nocache" -> Config.NoCache;
            case "vindexcache" -> Config.VIndexCache;
            default -> Config.BufferCache;
        };
    }
}
