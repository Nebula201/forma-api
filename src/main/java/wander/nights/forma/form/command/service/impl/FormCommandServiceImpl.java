package wander.nights.forma.form.command.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import wander.nights.forma.form.command.dto.FormCreateCommand;
import wander.nights.forma.form.command.entity.Form;
import wander.nights.forma.form.command.entity.FormCollaborator;
import wander.nights.forma.form.command.entity.FormRole;
import wander.nights.forma.form.command.repository.FormCollaboratorRepository;
import wander.nights.forma.form.command.repository.FormRepository;
import wander.nights.forma.form.command.repository.FormRoleRepository;
import wander.nights.forma.form.command.service.FormCommandService;
import wander.nights.forma.form.command.service.FormFactory;
import wander.nights.forma.shared.valueobject.FormId;
import wander.nights.forma.shared.valueobject.UserId;

@Service
@RequiredArgsConstructor
public class FormCommandServiceImpl implements FormCommandService {
    private final FormRepository formRepository;
    private final FormRoleRepository formRoleRepository;
    private final FormCollaboratorRepository formCollaboratorRepository;
    private final FormFactory formFactory;

    @Override

    public FormId createForm(UserId userId, FormCreateCommand request) {
        Form form = formFactory.createForm(request.getCode(), request.getTitle(), request.getDescription());
        FormId formId = form.getFormId();

        FormRole formRole = formFactory.createFormOwner(formId);
        FormCollaborator owner = formFactory.createFormCollaborator(formId, userId, formRole);

        formRoleRepository.save(formRole);
        formRepository.save(form);
        formCollaboratorRepository.save(owner);
        return formId;
    }

    @Override
    public void deleteForm(FormId formId) {
        formRepository.deleteById(formId);
    }


}
