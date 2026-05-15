package wander.nights.forma.form.command.service;

import wander.nights.forma.form.command.dto.FormRequests;
import wander.nights.forma.shared.valueobject.FormId;

public interface FormCommandService {

    FormId createForm(FormRequests.FormCreateRequest request);
}
