package wander.nights.forma.form.command.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import wander.nights.forma.form.command.dto.FormCreateCommand;
import wander.nights.forma.form.command.repository.FormRepository;
import wander.nights.forma.shared.valueobject.FormId;
import wander.nights.forma.shared.valueobject.UserId;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FormCommandServiceTest {
    @Autowired
    private FormCommandService formCommandService;
    @Autowired
    private FormRepository formRepository;

    @Test
    void testCreateForm() {
        UserId userId = new UserId("123");

        FormCreateCommand command = new FormCreateCommand();
        command.setCode("test");
        command.setTitle("测试表单");
        command.setDescription("测试表单描述");
        FormId formId = formCommandService.createForm(userId, command);
        assertNotNull(formId);
        formCommandService.deleteForm(formId);
        assertTrue(formRepository.findById(formId).isEmpty());
    }

}