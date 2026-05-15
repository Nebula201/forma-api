package wander.nights.forma.form.command.service.impl;

import org.springframework.stereotype.Service;
import wander.nights.forma.form.command.dto.FormRequests;
import wander.nights.forma.form.command.service.FormCommandService;
import wander.nights.forma.shared.valueobject.FormId;

@Service
public class FormCommandServiceImpl implements FormCommandService {
    @Override
    public FormId createForm(FormRequests.FormCreateRequest request) {
        return null;
    }
}
