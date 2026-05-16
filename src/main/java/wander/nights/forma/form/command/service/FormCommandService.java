package wander.nights.forma.form.command.service;

import wander.nights.forma.form.command.dto.FormCreateCommand;
import wander.nights.forma.shared.valueobject.FormId;
import wander.nights.forma.shared.valueobject.UserId;

public interface FormCommandService {

    FormId createForm(UserId userId, FormCreateCommand request);


    void deleteForm(FormId formId);
}
