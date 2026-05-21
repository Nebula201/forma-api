package wander.nights.forma.form.command.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import wander.nights.forma.form.command.dto.CollaboratorAddCommand;
import wander.nights.forma.form.command.entity.FormCollaborator;
import wander.nights.forma.form.command.repository.FormCollaboratorRepository;
import wander.nights.forma.form.command.service.FormCollaboratorCommandService;
import wander.nights.forma.shared.exception.OperationNotAllowException;
import wander.nights.forma.shared.identifier.FormId;
import wander.nights.forma.shared.identifier.OperatorId;
import wander.nights.forma.shared.valueobject.FormRoleCode;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FormCollaboratorCommandServiceImpl implements FormCollaboratorCommandService {
    private final FormCollaboratorRepository formCollaboratorRepository;

    @Override
    public UUID addCollaborator(FormId formId, CollaboratorAddCommand request) {
        FormCollaborator collaborator = new FormCollaborator();
        collaborator.setUserId(request.getUserId());
        collaborator.setFormId(formId);
        collaborator.setRoleCode(request.getRoleCode());
        formCollaboratorRepository.save(collaborator);
        return collaborator.getId();
    }

    @Override
    public void deleteCollaborator(FormId formId, OperatorId userId) {
        Optional<FormCollaborator> optionalFormCollaborator = formCollaboratorRepository.findByFormIdAndUserId(formId, userId);
        if (optionalFormCollaborator.isEmpty()) return;

        FormCollaborator collaborator = optionalFormCollaborator.get();
        if (FormRoleCode.OWNER.equals(collaborator.getRoleCode())) {
            throw new OperationNotAllowException("不允许删除表单所有者");
        }

        formCollaboratorRepository.delete(collaborator);
    }
}
