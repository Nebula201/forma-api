package wander.nights.forma.shared.valueobject;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 表单角色编码
 *
 * @param value 编码
 */
public record FormRoleCode(
        @JsonValue
        String value
) {

    public static FormRoleCode OWNER = new FormRoleCode("owner");
    public static FormRoleCode ADMIN = new FormRoleCode("admin");
    public static FormRoleCode VIEWER = new FormRoleCode("viewer");
}
