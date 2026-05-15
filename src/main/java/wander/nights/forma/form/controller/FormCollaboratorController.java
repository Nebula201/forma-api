package wander.nights.forma.form.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import wander.nights.forma.form.command.dto.FormCollaboratorRequests;
import wander.nights.forma.form.command.entity.FormCollaborator;
import wander.nights.forma.form.command.repository.FormCollaboratorRepository;
import wander.nights.forma.form.command.service.FormCollaboratorCommandService;
import wander.nights.forma.form.query.service.FormCollaboratorReadService;
import wander.nights.forma.shared.response.Result;
import wander.nights.forma.shared.valueobject.FormId;

import java.util.List;


@Tag(name = "表单协作管理")
@RestController
@RequestMapping("/v1/forms/{formId}/collaborators")
@Setter(onMethod_ = @Autowired)
public class FormCollaboratorController {
    private FormCollaboratorCommandService formCollaboratorCommandService;
    private FormCollaboratorReadService formCollaboratorReadService;
    private FormCollaboratorRepository formCollaboratorRepository;


    @Operation(summary = "获取表单协作者")
    @GetMapping
    public Result<List<FormCollaborator>> listCollaborator(@PathVariable FormId formId, FormCollaboratorRequests.Query query) {
        List<FormCollaborator> collaborators = formCollaboratorRepository.findByFormId(formId);
        return Result.ok(collaborators);
    }

    @Operation(summary = "新增协作者")
    @PostMapping
    public Result<?> addCollaborator(@PathVariable FormId formId, @RequestBody FormCollaboratorRequests.AddRequest request) {
        FormCollaborator formCollaborator = formCollaboratorRepository.findByFormIdAndUserId(formId, request.getUserId())
                .orElse(null);
        if (formCollaborator != null) {

        }
        FormCollaborator collaborator = new FormCollaborator();
        collaborator.setUserId(request.getUserId());
        collaborator.setFormId(formId);
        collaborator.setRoleCode(request.getRoleCode());
        formCollaboratorRepository.save(collaborator);

        return Result.ok(collaborator.getId());
    }
}
