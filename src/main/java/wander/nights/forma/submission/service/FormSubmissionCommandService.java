package wander.nights.forma.submission.service;

import wander.nights.forma.form.command.dto.SubmissionSubmitCommand;
import wander.nights.forma.shared.identifier.FormId;
import wander.nights.forma.submission.domain.FormSubmissionId;

public interface FormSubmissionCommandService {

    FormSubmissionId submit(FormId formId, SubmissionSubmitCommand request);
}
