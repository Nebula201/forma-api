package wander.nights.forma.form.command.service;

import wander.nights.forma.form.command.dto.RoleCreateCommand;
import wander.nights.forma.shared.valueobject.FormId;
import wander.nights.forma.shared.valueobject.FormRoleCode;

public interface FormRoleCommandService {

    FormRoleCode addRole(FormId formId, RoleCreateCommand command);

    void deleteRole(FormId formId, FormRoleCode roleCode);


}
