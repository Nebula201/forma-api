package wander.nights.forma.form.command.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import wander.nights.forma.form.command.dto.SubmissionSubmitRequest;
import wander.nights.forma.form.command.entity.FormSubmission;
import wander.nights.forma.form.command.repository.FormSubmissionRepository;
import wander.nights.forma.model.FormContent;
import wander.nights.forma.model.fields.FieldDefinition;
import wander.nights.forma.form.command.service.FormSubmissionCommandService;
import wander.nights.forma.shared.valueobject.FieldCode;
import wander.nights.forma.shared.valueobject.FormId;
import wander.nights.forma.shared.valueobject.FormSubmissionId;

import java.time.Instant;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FormSubmissionCommandServiceImpl implements FormSubmissionCommandService {
    private final FormSubmissionRepository formSubmissionRepository;

    @Override
    public FormSubmissionId submit(FormId formId, SubmissionSubmitRequest request) {
        FormSubmission formSubmission = new FormSubmission();
        formSubmission.setFormSubmissionId(formSubmissionRepository.nextId());
        formSubmission.setFormId(formId);
        formSubmission.setFormVersion(request.getFormVersion());
        formSubmission.setContent(request.getContent());
        formSubmission.setDurationSecond(request.getDurationSecond());
        formSubmission.setSubmittedAt(Instant.now());
        formSubmissionRepository.save(formSubmission);
        return formSubmission.getFormSubmissionId();
    }
}
