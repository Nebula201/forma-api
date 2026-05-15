package wander.nights.forma.form.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import wander.nights.forma.form.command.dto.SubmissionSubmitRequest;
import wander.nights.forma.dto.response.SubmissionVo;
import wander.nights.forma.form.query.dto.FormSubmissionReadRequests;
import wander.nights.forma.form.query.service.FormSubmissionReadService;
import wander.nights.forma.form.command.service.FormSubmissionCommandService;
import wander.nights.forma.shared.response.Result;
import wander.nights.forma.shared.valueobject.FormId;
import wander.nights.forma.shared.valueobject.FormSubmissionId;

@Tag(name = "表单数据管理")
@RestController
@RequestMapping("/v1/forms/{formId}/submissions")
@Setter(onMethod_ = @Autowired)
public class SubmissionController {
    private FormSubmissionCommandService formSubmissionCommandService;
    private FormSubmissionReadService formSubmissionReadService;

    @Operation(summary = "提交数据")
    @PostMapping
    public Result<FormSubmissionId> submit(
            @PathVariable FormId formId,
            @Validated @RequestBody SubmissionSubmitRequest request
    ) {
        return Result.ok(formSubmissionCommandService.submit(formId, request));
    }

    public Result<Page<SubmissionVo>> page(
            @PathVariable("formId") FormId formId,
            FormSubmissionReadRequests.Query query) {
        return new Result<>();
    }
}
