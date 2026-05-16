package wander.nights.forma.form.query.service;

import wander.nights.forma.form.query.dto.collaborator.CollaboratorVo;
import wander.nights.forma.form.query.dto.collaborator.QueryCollaborator;
import wander.nights.forma.shared.valueobject.FormId;

import java.util.List;

public interface FormCollaboratorReadService {

    List<CollaboratorVo> listCollaborator(FormId formId, QueryCollaborator query);
}
