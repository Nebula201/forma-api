package wander.nights.forma.form.command.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wander.nights.forma.event.EventPublisher;
import wander.nights.forma.event.FormaEvent;
import wander.nights.forma.event.payload.FormCreatedV1;
import wander.nights.forma.event.payload.FormDeletedV1;
import wander.nights.forma.form.command.dto.FormCreateCommand;
import wander.nights.forma.form.command.entity.Form;
import wander.nights.forma.form.command.entity.FormCollaborator;
import wander.nights.forma.form.command.entity.FormRole;
import wander.nights.forma.form.command.repository.FormCollaboratorRepository;
import wander.nights.forma.form.command.repository.FormRepository;
import wander.nights.forma.form.command.repository.FormRoleRepository;
import wander.nights.forma.form.command.service.FormCommandService;
import wander.nights.forma.form.command.service.FormFactory;
import wander.nights.forma.shared.context.RequestContext;
import wander.nights.forma.shared.identifier.FormId;
import wander.nights.forma.shared.identifier.OperatorId;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class FormCommandServiceImpl implements FormCommandService {
    private final FormRepository formRepository;
    private final FormRoleRepository formRoleRepository;
    private final FormCollaboratorRepository formCollaboratorRepository;
    private final FormFactory formFactory;
    private final EventPublisher eventPublisher;

    @Override
    @Transactional
    public FormId createForm(OperatorId userId, FormCreateCommand request) {
        Form form = formFactory.createForm(request.getCode(), request.getTitle(), request.getDescription());
        FormId formId = form.getFormId();

        FormRole formRole = formFactory.createFormOwner(formId);
        FormCollaborator owner = formFactory.createFormCollaborator(formId, userId, formRole);

        formRoleRepository.save(formRole);
        formRepository.save(form);
        formCollaboratorRepository.save(owner);

        publishFormCreatedEvent(form, userId);

        return formId;
    }

    private void publishFormCreatedEvent(Form form, OperatorId userId) {
        FormCreatedV1 payload = new FormCreatedV1();
        payload.setFormId(form.getFormId().value().toString());
        payload.setFormCode(form.getCode());
        payload.setFormName(form.getTitle());
        payload.setFormDescription(form.getDescription());
        payload.setCreatedBy(userId.value());
        payload.setCreatedAt(Instant.now());

        FormaEvent<FormCreatedV1> event = new FormaEvent<>(payload);


        eventPublisher.publish(event);
    }

    @Override
    @Transactional
    public void deleteForm(FormId formId) {
        formRepository.deleteById(formId);
        FormDeletedV1 payload = new FormDeletedV1();
        payload.setFormId(formId);
        payload.setDeletedBy(RequestContext.currentOperatorId());
        payload.setDeletedAt(Instant.now());
        eventPublisher.publish(new FormaEvent<>(payload));
    }

    @Override
    public void release(FormId formId) {

    }


}
