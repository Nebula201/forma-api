package wander.nights.forma.form.command.service;

import wander.nights.forma.form.command.dto.SubmissionSubmitCommand;
import wander.nights.forma.shared.valueobject.FormId;
import wander.nights.forma.shared.valueobject.FormSubmissionId;

public interface FormSubmissionCommandService {

    FormSubmissionId submit(FormId formId, SubmissionSubmitCommand request);
}
