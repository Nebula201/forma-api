package wander.nights.forma.form.command.dto;

import lombok.Data;
import wander.nights.forma.form.command.entity.FormRole;
import wander.nights.forma.shared.valueobject.FormRoleCode;

import java.util.Set;

@Data
public class RoleCreateCommand {
    private FormRoleCode roleCode;
    private String roleName;
    private Set<String> operations;
    private FormRole.AccessPermission accessPermission;
}
