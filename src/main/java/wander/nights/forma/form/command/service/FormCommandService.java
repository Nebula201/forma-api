package wander.nights.forma.form.command.service;

import wander.nights.forma.form.command.dto.FormCreateCommand;
import wander.nights.forma.shared.identifier.FormId;
import wander.nights.forma.shared.identifier.OperatorId;

public interface FormCommandService {

    FormId createForm(OperatorId userId, FormCreateCommand request);


    void deleteForm(FormId formId);


    void release(FormId formId);
}
