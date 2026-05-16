package wander.nights.forma.form.command.service;

import wander.nights.forma.form.command.dto.CollaboratorAddCommand;
import wander.nights.forma.shared.valueobject.FormId;
import wander.nights.forma.shared.valueobject.UserId;

import java.util.UUID;

public interface FormCollaboratorCommandService {

    UUID addCollaborator(FormId formId, CollaboratorAddCommand request);

    void deleteCollaborator(FormId formId, UserId userId);
}
