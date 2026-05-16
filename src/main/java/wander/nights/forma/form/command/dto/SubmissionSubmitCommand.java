package wander.nights.forma.form.command.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import wander.nights.forma.shared.valueobject.FieldCode;

import java.util.Map;

/**
 * 数据提交请求
 */
@Schema(name = "数据提交请求实体")
@Data
public class SubmissionSubmitCommand {
    @Schema(description = "表单版本", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer formVersion;
    @Schema(description = "数据内容", requiredMode = Schema.RequiredMode.REQUIRED)
    private Map<FieldCode, Object> content;
    @Schema(description = "填写时长", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private int durationSecond;
}
