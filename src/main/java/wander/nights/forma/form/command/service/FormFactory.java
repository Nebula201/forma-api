package wander.nights.forma.form.command.service;

import com.github.f4b6a3.uuid.alt.GUID;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wander.nights.forma.form.command.dto.RoleCreateCommand;
import wander.nights.forma.form.command.entity.Form;
import wander.nights.forma.form.command.entity.FormCollaborator;
import wander.nights.forma.form.command.entity.FormRole;
import wander.nights.forma.form.command.entity.FormOperationPermission;
import wander.nights.forma.shared.identifier.FormId;
import wander.nights.forma.shared.identifier.OperatorId;
import wander.nights.forma.shared.valueobject.FormRoleCode;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Setter(onMethod_ = @Autowired)
public class FormFactory {

    public FormId nextFormId() {
        return new FormId(GUID.v7().toUUID());
    }


    public Form createForm(String code, String title, String description) {
        Form form = new Form();
        form.setFormId(nextFormId());
        form.setCode(code);
        form.setTitle(title);
        form.setDescription(description);
        form.setStatus(Form.Status.DRAFT);
        return form;
    }

    public FormRole createFormOwner(FormId formId) {
        FormRole formRole = new FormRole();
        formRole.setFormId(formId);
        formRole.setCode(new FormRoleCode("owner"));
        formRole.setName("所有者");
        formRole.setOperationPermissions(FormOperationPermission.ALL);
        return formRole;
    }

    public FormRole creatFormAdmin(FormId formId) {
        FormRole formRole = new FormRole();
        formRole.setFormId(formId);
        formRole.setCode(new FormRoleCode("admin"));
        formRole.setName("管理员");
        formRole.setOperationPermissions(FormOperationPermission.ALL);
        return formRole;
    }

    public FormRole creatFormViewer(FormId formId) {
        FormRole formRole = new FormRole();
        formRole.setFormId(formId);
        formRole.setCode(new FormRoleCode("viewer"));
        formRole.setName("数据浏览员");
        Set<FormOperationPermission> set = Set.of(
                FormOperationPermission.SUBMISSION_VIEW
        );
        formRole.setOperationPermissions(set);

        return formRole;
    }


    public FormRole createFormRole(FormId formId, RoleCreateCommand command) {
        FormRole formRole = new FormRole();
        formRole.setFormId(formId);
        formRole.setCode(command.getRoleCode());
        formRole.setName(command.getRoleName());
        formRole.setAccessPermissions(command.getAccessPermission());
        formRole.setOperationPermissions(command.getOperations().stream()
                .map(FormOperationPermission::fromCode)
                .filter(Objects::nonNull).collect(Collectors.toSet())
        );
        return formRole;
    }

    public FormCollaborator createFormCollaborator(FormId formId, OperatorId userId, FormRole role) {
        FormCollaborator collaborator = new FormCollaborator();
        collaborator.setFormId(formId);
        collaborator.setOperatorId(userId);
        collaborator.setRoleCode(role.getCode());
        return collaborator;
    }


}
