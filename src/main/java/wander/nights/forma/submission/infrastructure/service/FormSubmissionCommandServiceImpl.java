package wander.nights.forma.submission.infrastructure.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wander.nights.forma.event.EventPublisher;
import wander.nights.forma.event.FormaEvent;
import wander.nights.forma.submission.event.SubmissionSubmittedV1;
import wander.nights.forma.form.command.dto.SubmissionSubmitCommand;
import wander.nights.forma.submission.domain.FormSubmission;
import wander.nights.forma.form.command.entity.FormVersion;
import wander.nights.forma.submission.domain.FormSubmissionRepository;
import wander.nights.forma.form.command.repository.FormVersionRepository;
import wander.nights.forma.form.command.service.FormFactory;
import wander.nights.forma.submission.service.FormSubmissionCommandService;
import wander.nights.forma.model.FormContent;
import wander.nights.forma.shared.identifier.FieldCode;
import wander.nights.forma.shared.identifier.FormId;
import wander.nights.forma.submission.domain.FormSubmissionId;
import wander.nights.forma.submission.service.SubmissionNoProvider;

import java.time.Instant;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FormSubmissionCommandServiceImpl implements FormSubmissionCommandService {
    private final FormSubmissionRepository formSubmissionRepository;
    private final FormFactory formFactory;
    private final FormVersionRepository formVersionRepository;
    private final EventPublisher eventPublisher;
    private final SubmissionNoProvider submissionNoProvider;

    @Override
    @Transactional
    public FormSubmissionId submit(FormId formId, SubmissionSubmitCommand request) {
        FormVersion formVersion = formVersionRepository.findByFormIdAndFormVersion(formId, request.getFormVersion())
                .orElseThrow();

        // 校验数据
        this.validate(formVersion.getFormContent(), request.getContent());
        FormSubmissionId formSubmissionId = new FormSubmissionId(formId, Math.toIntExact(submissionNoProvider.nextSubmissionNo(formId)));
        FormSubmission formSubmission = new FormSubmission();
        formSubmission.setFormSubmissionId(formSubmissionId);
        formSubmission.setFormVersion(request.getFormVersion());
        formSubmission.setContent(request.getContent());
        formSubmission.setDurationSecond(request.getDurationSecond());
        formSubmission.setSubmittedAt(Instant.now());
        formSubmissionRepository.save(formSubmission);

        publishSubmissionSubmittedEvent(formSubmission);

        return formSubmissionId;
    }

    private void publishSubmissionSubmittedEvent(FormSubmission formSubmission) {
        SubmissionSubmittedV1 payload = new SubmissionSubmittedV1(formSubmission);
        eventPublisher.publish(new FormaEvent<>(payload));
    }

    private void validate(FormContent formContent, Map<FieldCode, Object> content) {

    }
}
