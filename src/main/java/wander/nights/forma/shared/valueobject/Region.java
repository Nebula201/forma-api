package wander.nights.forma.shared.valueobject;

public record Region(
        String country,//国家
        String province,// 省份
        String city, // 城市
        String countryCode, // 国家代码
        String isp // 互联网服务提供商
) {
    public static final String UNKNOWN_STR = "UNKNOWN";
    public static final String PRIVATE_STR = "PRIVATE";
    public static final Region UNKNOWN = new Region(UNKNOWN_STR, UNKNOWN_STR, UNKNOWN_STR, UNKNOWN_STR, UNKNOWN_STR);
    public static final Region PRIVATE = new Region(PRIVATE_STR, PRIVATE_STR, PRIVATE_STR, PRIVATE_STR, PRIVATE_STR);

}
