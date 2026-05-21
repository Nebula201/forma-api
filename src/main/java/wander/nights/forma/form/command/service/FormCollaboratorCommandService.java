package wander.nights.forma.form.command.service;

import wander.nights.forma.form.command.dto.CollaboratorAddCommand;
import wander.nights.forma.shared.identifier.FormId;
import wander.nights.forma.shared.identifier.OperatorId;

import java.util.UUID;

public interface FormCollaboratorCommandService {

    UUID addCollaborator(FormId formId, CollaboratorAddCommand request);

    void deleteCollaborator(FormId formId, OperatorId userId);
}
