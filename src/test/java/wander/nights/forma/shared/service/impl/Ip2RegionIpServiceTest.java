package wander.nights.forma.shared.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import wander.nights.forma.shared.valueobject.Region;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest(classes = {Ip2RegionIpService.class})
@EnableConfigurationProperties(Ip2RegionIpService.Ip2RegionProperties.class)
@TestPropertySource(properties = {
        "ip2region.v4.enable=true",
        "ip2region.v4.db-path=classpath:ip2region_v4.xdb",
        "ip2region.v6.enable=true",
        "ip2region.v6.db-path=classpath:ip2region_v6.xdb",
})
class Ip2RegionIpServiceTest {

    @Autowired
    private Ip2RegionIpService ipService;

    @ParameterizedTest
    @ValueSource(strings = {
            "113.92.157.29",
            "114.114.114.114"
    })
    @DisplayName("输出解析结果")
    void testPrintRegion(String host) {
        log.info("IP [{}] 解析结果:{}", host, ipService.resolveIpAddress(host));
    }

    @Test
    @DisplayName("测试输入为 null 的情况")
    void testNullIpAddress() {
        assertEquals(Region.UNKNOWN, ipService.resolveIpAddress((String) null), "输入 null 时应返回 Region.UNKNOWN");
        assertEquals(Region.UNKNOWN, ipService.resolveIpAddress((InetAddress) null), "输入 null 时应返回 Region.UNKNOWN");
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "10.0.0.1",
            "10.255.255.255",
            "192.168.1.100",
            "192.168.0.1",
            "172.16.0.1",
            "172.31.255.255"
    })
    @DisplayName("测试合法 IPv4 内网地址")
    void testPrivateIpv4Addresses(String address) throws UnknownHostException {
        Region r1 = ipService.resolveIpAddress(address);
        Region r2 = ipService.resolveIpAddress(InetAddress.getByName(address));
        assertEquals(Region.PRIVATE, r1, "地址 " + address + " 应该被识别为私有地址");
        assertEquals(Region.PRIVATE, r2, "地址 " + address + " 应该被识别为私有地址");
        assertEquals(r1, r2, "地址 " + address + "两个方法返回结果应当一致");
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "::1",
            "fe80::1",
            "fc00::1",
            "fd00::1",
            "2001:db8::1"
    })
    @DisplayName("测试 IPv6 内网地址")
    void testPrivateIpv6Addresses(String address) throws UnknownHostException {
        // IPv6 私有地址
        Region r1 = ipService.resolveIpAddress(address);
        Region r2 = ipService.resolveIpAddress(InetAddress.getByName(address));
        assertEquals(Region.PRIVATE, r1, "地址 " + address + " 应该被识别为私有地址");
        assertEquals(Region.PRIVATE, r2, "地址 " + address + " 应该被识别为私有地址");
        assertEquals(r1, r2, "地址 " + address + "两个方法返回结果应当一致");
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "256.1.1.1",
            "999.999.999.999",
            "abc.def.ghi.jkl",
            "",
            "127.0.0.1",
            "192.168.1.1.1",
            "192.168.1"
    })
    @DisplayName("测试非法 IP 地址")
    void testInvalidIpAddress(String invalidIp) throws UnknownHostException {
//        assertThrows(UnknownHostException.class, () -> InetAddress.getByName(invalidIp), "地址" + invalidIp + "应是无效地址");
        assertEquals(Region.UNKNOWN, ipService.resolveIpAddress(invalidIp), "地址 " + invalidIp + " 应该被识别为无效地址");
    }

    static Stream<Arguments> testPublicIpAddress() {
        return Stream.of(
                Arguments.of("8.8.8.8", new Region("United States", "California", "UNKNOWN", "US", "Google LLC")),
                Arguments.of("1.1.1.1", new Region("Australia", "Queensland", "Brisbane", "AU", "UNKNOWN")),
                Arguments.of("113.92.157.29", new Region("中国", "广东省", "深圳市", "CN", "电信")),
//                Arguments.of("www.baidu.com", new Region("中国", "广东省", "广州市", "CN", "电信")),
                Arguments.of("114.114.114.114", new Region("中国", "江苏省", "南京市", "CN", "UNKNOWN"))
        );
    }

    @ParameterizedTest
    @MethodSource
    @DisplayName("测试有名的公网 IP")
    void testPublicIpAddress(String ip, Region result) throws UnknownHostException {
        // 一些公网 IP（可能是无效或需要数据库才能解析）
        Region region = ipService.resolveIpAddress(InetAddress.getByName(ip));
        assertEquals(result, region, "地址 " + ip + " 结果与预期不符");
    }
}