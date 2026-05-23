package wander.nights.forma.form.command.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import wander.nights.forma.shared.identifier.OperatorId;
import wander.nights.forma.shared.valueobject.FormRoleCode;

@Data
public class CollaboratorAddCommand {
    @NotNull(message = "角色编码不能为空")
    private FormRoleCode roleCode;
    @NotNull
    private OperatorId operatorId;
}
