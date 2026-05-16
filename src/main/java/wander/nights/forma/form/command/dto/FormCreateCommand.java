package wander.nights.forma.form.command.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class FormCreateCommand {
    @NotBlank(message = "表单编码不能为空")
    @Size(max = 32, message = "表单编码长度不能超过32")
    private String code;

    @NotBlank(message = "表单标题不能为空")
    @Size(max = 255, message = "表单标题长度不能超过255")
    private String title;

    private String description;
}