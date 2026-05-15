package wander.nights.forma.form.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import wander.nights.forma.form.command.entity.FormRole;
import wander.nights.forma.form.command.service.FormRoleCommandService;
import wander.nights.forma.form.query.service.FormRoleReadService;
import wander.nights.forma.shared.exception.ResourceNotFoundException;
import wander.nights.forma.shared.response.Result;
import wander.nights.forma.shared.valueobject.FormId;

import java.util.List;

@Tag(name = "表单角色管理")
@RestController
@RequestMapping("/v1/forms/{formId}/roles")
@Setter(onMethod_ = @Autowired)
public class FormRoleController {
    private FormRoleCommandService formRoleCommandService;
    private FormRoleReadService formRoleReadService;

//    @GetMapping
//    public Result<List<FormRole>> listFormRole(@PathVariable FormId formId) {
//        List<FormRole> formRoles = formRoleRepository.findByFormId(formId);
//        return Result.ok(formRoles);
//    }
//
//
////    @PostMapping
////    public Result<UUID> createRole() {
////
////    }
//
//    @GetMapping("/{code}")
//    public Result<FormRole> one(@PathVariable FormId formId, @PathVariable String code) {
//        return Result.ok(formRoleRepository.findByFormIdAndRoleCode(formId, code)
//                .orElseThrow(() -> new ResourceNotFoundException("")));
//    }
//
//    @DeleteMapping("/{code}")
//    public Result<?> deleteOne(@PathVariable FormId formId, @PathVariable String code) {
//        formRoleRepository.findByFormIdAndRoleCode(formId, code).ifPresent(
//                item -> formRoleRepository.delete(item)
//        );
//        return Result.ok("");
//    }


}
