package wander.nights.forma.form.command.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import wander.nights.forma.form.command.dto.RoleCreateCommand;
import wander.nights.forma.form.command.entity.FormRole;
import wander.nights.forma.form.command.repository.FormRoleRepository;
import wander.nights.forma.form.command.service.FormFactory;
import wander.nights.forma.form.command.service.FormRoleCommandService;
import wander.nights.forma.shared.exception.OperationNotAllowException;
import wander.nights.forma.shared.valueobject.FormId;
import wander.nights.forma.shared.valueobject.FormRoleCode;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FormRoleCommandServiceImpl implements FormRoleCommandService {
    private final FormRoleRepository formRoleRepository;
    private final FormFactory formFactory;

    @Override
    public FormRoleCode addRole(FormId formId, RoleCreateCommand command) {
        FormRole formRole = formFactory.createFormRole(formId, command);
        formRoleRepository.save(formRole);
        return formRole.getCode();
    }

    @Override
    public void deleteRole(FormId formId, FormRoleCode roleCode) {
        Optional<FormRole> optional = formRoleRepository.findByFormIdAndCode(formId, roleCode);

        if (optional.isEmpty()) return;
        FormRole role = optional.get();
        if (FormRoleCode.OWNER.equals(roleCode)) {
            throw new OperationNotAllowException("不能删除此角色");
        }
        formRoleRepository.delete(role);
    }


}
