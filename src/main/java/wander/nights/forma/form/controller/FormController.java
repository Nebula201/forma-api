package wander.nights.forma.form.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import wander.nights.forma.form.command.dto.FormCreateCommand;
import wander.nights.forma.form.command.service.FormCommandService;
import wander.nights.forma.form.query.service.FormReadService;
import wander.nights.forma.shared.response.Result;
import wander.nights.forma.shared.valueobject.FormId;
import wander.nights.forma.shared.valueobject.UserId;

@Tag(name = "表单管理")
@RestController
@RequestMapping("/v1/forms")
@Setter(onMethod_ = @Autowired)
public class FormController {
    private FormCommandService formCommandService;
    private FormReadService formReadService;

    @Operation(summary = "创建表单")
    @PostMapping
    public Result<FormId> createForm(@Validated @RequestBody FormCreateCommand request) {
        UserId userId = new UserId("1");
        return Result.ok(formCommandService.createForm(userId, request));
    }
}
