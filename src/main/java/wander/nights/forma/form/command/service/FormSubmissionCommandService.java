package wander.nights.forma.form.command.service;

import wander.nights.forma.form.command.dto.SubmissionSubmitRequest;
import wander.nights.forma.model.FormContent;
import wander.nights.forma.shared.valueobject.FieldCode;
import wander.nights.forma.shared.valueobject.FormId;
import wander.nights.forma.shared.valueobject.FormSubmissionId;

import java.util.Map;

public interface FormSubmissionCommandService {

    FormSubmissionId submit(FormId formId, SubmissionSubmitRequest request);
}
