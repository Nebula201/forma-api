package wander.nights.forma.form.query.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import wander.nights.forma.form.query.dto.collaborator.CollaboratorVo;
import wander.nights.forma.form.query.dto.collaborator.QueryCollaborator;
import wander.nights.forma.form.query.service.FormCollaboratorReadService;
import wander.nights.forma.shared.valueobject.FormId;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FormCollaboratorReadServiceImpl implements FormCollaboratorReadService {
    @Override
    public List<CollaboratorVo> listCollaborator(FormId formId, QueryCollaborator query) {
        return List.of();
    }
}
