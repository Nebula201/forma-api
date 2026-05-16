package wander.nights.forma.form.command.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import wander.nights.forma.form.command.dto.SubmissionSubmitCommand;
import wander.nights.forma.form.command.entity.FormSubmission;
import wander.nights.forma.form.command.entity.FormVersion;
import wander.nights.forma.form.command.repository.FormSubmissionRepository;
import wander.nights.forma.form.command.repository.FormVersionRepository;
import wander.nights.forma.form.command.service.FormFactory;
import wander.nights.forma.form.command.service.FormSubmissionCommandService;
import wander.nights.forma.model.FormContent;
import wander.nights.forma.shared.valueobject.FieldCode;
import wander.nights.forma.shared.valueobject.FormId;
import wander.nights.forma.shared.valueobject.FormSubmissionId;

import java.time.Instant;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FormSubmissionCommandServiceImpl implements FormSubmissionCommandService {
    private final FormSubmissionRepository formSubmissionRepository;
    private final FormFactory formFactory;
    private final FormVersionRepository formVersionRepository;

    @Override
    public FormSubmissionId submit(FormId formId, SubmissionSubmitCommand request) {
        FormVersion formVersion = formVersionRepository.findByFormIdAndFormVersion(formId, request.getFormVersion())
                .orElseThrow();

        // 校验数据
        this.validate(formVersion.getFormContent(), request.getContent());

        FormSubmission formSubmission = new FormSubmission();
        formSubmission.setFormSubmissionId(formFactory.nextSubmissionId());
        formSubmission.setFormId(formId);
        formSubmission.setFormVersion(request.getFormVersion());
        formSubmission.setContent(request.getContent());
        formSubmission.setDurationSecond(request.getDurationSecond());
        formSubmission.setSubmittedAt(Instant.now());
        formSubmissionRepository.save(formSubmission);
        return formSubmission.getFormSubmissionId();
    }

    private void validate(FormContent formContent, Map<FieldCode, Object> content) {

    }
}
