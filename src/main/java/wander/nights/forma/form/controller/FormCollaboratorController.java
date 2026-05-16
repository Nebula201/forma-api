package wander.nights.forma.form.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import wander.nights.forma.form.command.dto.CollaboratorAddCommand;
import wander.nights.forma.form.command.service.FormCollaboratorCommandService;
import wander.nights.forma.form.query.dto.collaborator.CollaboratorVo;
import wander.nights.forma.form.query.dto.collaborator.QueryCollaborator;
import wander.nights.forma.form.query.service.FormCollaboratorReadService;
import wander.nights.forma.shared.response.Result;
import wander.nights.forma.shared.valueobject.FormId;
import wander.nights.forma.shared.valueobject.UserId;

import java.util.List;
import java.util.UUID;


@Tag(name = "表单协作管理")
@RestController
@RequestMapping("/v1/forms/{formId}/collaborators")
@Setter(onMethod_ = @Autowired)
public class FormCollaboratorController {
    private FormCollaboratorCommandService formCollaboratorCommandService;
    private FormCollaboratorReadService formCollaboratorReadService;

    @Operation(summary = "获取表单协作者")
    @GetMapping
    public Result<List<CollaboratorVo>> listCollaborator(
            @PathVariable FormId formId,
            QueryCollaborator query) {
        return Result.ok(formCollaboratorReadService.listCollaborator(formId, query));
    }

    @Operation(summary = "新增协作者")
    @PostMapping
    public Result<UUID> addCollaborator(
            @PathVariable FormId formId,
            @RequestBody CollaboratorAddCommand request) {
        return Result.ok(formCollaboratorCommandService.addCollaborator(formId, request));
    }

    @Operation(summary = "移除协作者")
    @DeleteMapping("/{userId}")
    public Result<?> delete(@PathVariable FormId formId,
                            @PathVariable UserId userId) {
        formCollaboratorCommandService.deleteCollaborator(formId, userId);
        return Result.ok("");
    }
}
