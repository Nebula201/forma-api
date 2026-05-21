package wander.nights.forma.form.command.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import wander.nights.forma.form.command.dto.FormCreateCommand;
import wander.nights.forma.form.command.entity.Form;
import wander.nights.forma.form.command.repository.FormRepository;
import wander.nights.forma.shared.context.RequestContext;
import wander.nights.forma.shared.context.UserAttributes;
import wander.nights.forma.shared.identifier.FormId;
import wander.nights.forma.shared.identifier.OperatorId;

import java.net.UnknownHostException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FormCommandServiceTest {
    @Autowired
    private FormCommandService formCommandService;
    @Autowired
    private FormRepository formRepository;

    @BeforeEach
    void setUpRequestContext() throws UnknownHostException {
        // 设置 RequestContext 用于 deleteForm 方法
        OperatorId operatorId = OperatorId.of("123");
        UserAttributes userAttributes = UserAttributes.of(operatorId);
        RequestContext.set(new RequestContext(userAttributes, null));
    }

    @Test
    void testCreateForm() {
        OperatorId userId = new OperatorId("123");

        FormCreateCommand command = new FormCreateCommand();
        command.setCode("test");
        command.setTitle("测试表单");
        command.setDescription("测试表单描述");

        FormId formId = formCommandService.createForm(userId, command);
        assertNotNull(formId);

        // 验证删除
        formCommandService.deleteForm(formId);
        assertTrue(formRepository.findById(formId).isEmpty());
    }

    @Test
    void testCreateFormWithDifferentCode() {
        OperatorId OperatorId = new OperatorId("456");

        FormCreateCommand command = new FormCreateCommand();
        command.setCode("survey");
        command.setTitle("问卷调查");
        command.setDescription("用户问卷调查");

        FormId formId = formCommandService.createForm(OperatorId, command);
        assertNotNull(formId);

        formCommandService.deleteForm(formId);
        assertTrue(formRepository.findById(formId).isEmpty());
    }

    @Test
    void testCreateFormWithMinimalFields() {
        OperatorId OperatorId = new OperatorId("789");

        FormCreateCommand command = new FormCreateCommand();
        command.setCode("simple");
        command.setTitle("简单表单");

        FormId formId = formCommandService.createForm(OperatorId, command);
        assertNotNull(formId);

        formCommandService.deleteForm(formId);
        assertTrue(formRepository.findById(formId).isEmpty());
    }

    @Test
    void testCreateFormWithMultipleUsers() {
        // 用户1创建表单
        OperatorId OperatorId1 = new OperatorId("user1");

        FormCreateCommand command1 = new FormCreateCommand();
        command1.setCode("form1");
        command1.setTitle("表单1");
        command1.setDescription("用户1的表单");

        FormId formId1 = formCommandService.createForm(OperatorId1, command1);
        assertNotNull(formId1);

        // 用户2创建表单
        OperatorId OperatorId2 = new OperatorId("user2");

        FormCreateCommand command2 = new FormCreateCommand();
        command2.setCode("form2");
        command2.setTitle("表单2");
        command2.setDescription("用户2的表单");

        FormId formId2 = formCommandService.createForm(OperatorId2, command2);
        assertNotNull(formId2);

        // 验证两个表单都存在
        assertTrue(formRepository.findById(formId1).isPresent());
        assertTrue(formRepository.findById(formId2).isPresent());

        // 删除两个表单
        formCommandService.deleteForm(formId1);
        formCommandService.deleteForm(formId2);

        assertTrue(formRepository.findById(formId1).isEmpty());
        assertTrue(formRepository.findById(formId2).isEmpty());
    }

    @Test
    void testCreateFormWithEmptyCode() {
        OperatorId OperatorId = new OperatorId("111");

        FormCreateCommand command = new FormCreateCommand();
        command.setCode(""); // 测试空代码情况
        command.setTitle("空代码表单");
        command.setDescription("测试空代码");

        assertThrows(IllegalArgumentException.class, () -> {
            formCommandService.createForm(OperatorId, command);
        });
    }

    @Test
    void testCreateFormWithEmptyTitle() {
        OperatorId OperatorId = new OperatorId("222");

        FormCreateCommand command = new FormCreateCommand();
        command.setCode("valid-code");
        command.setTitle(""); // 测试空标题
        command.setDescription("测试空标题");

        assertThrows(IllegalArgumentException.class, () -> {
            formCommandService.createForm(OperatorId, command);
        });
    }

    @Test
    void testDeleteNonExistentForm() {
        FormId formId = new FormId(java.util.UUID.randomUUID());

        assertThrows(org.springframework.dao.EmptyResultDataAccessException.class, () -> {
            formCommandService.deleteForm(formId);
        });
    }

    @Test
    void testCreateAndQueryForm() {
        OperatorId OperatorId = new OperatorId("333");

        FormCreateCommand command = new FormCreateCommand();
        command.setCode("query-test");
        command.setTitle("查询测试表单");
        command.setDescription("用于测试查询的表单");

        FormId formId = formCommandService.createForm(OperatorId, command);

        // 查询验证
        assertTrue(formRepository.findById(formId).isPresent());

        Form form = formRepository.findById(formId).orElseThrow();
        assertEquals("query-test", form.getCode());
        assertEquals("查询测试表单", form.getTitle());
        assertEquals("用于测试查询的表单", form.getDescription());

        formCommandService.deleteForm(formId);
        assertTrue(formRepository.findById(formId).isEmpty());
    }

}