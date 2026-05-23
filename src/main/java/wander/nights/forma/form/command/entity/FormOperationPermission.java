package wander.nights.forma.form.command.entity;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.Set;

/**
 * 操作权限枚举
 */
@Getter
public enum FormOperationPermission {
    // 提交记录权限
    SUBMISSION_ADD("submission.add", "允许新建记录"),
    SUBMISSION_EDIT("submission.edit", "允许编辑记录"),
    SUBMISSION_DELETE("submission.delete", "允许删除记录"),
    SUBMISSION_VIEW("submission.view", "允许查看记录"),
    SUBMISSION_EXPORT("submission.export", "允许导出记录"),
    SUBMISSION_STATS("submission.stats", "允许数据统计"),

    // 表单管理权限
    FORM_EDIT("form.edit", "编辑表单"),
    FORM_DELETE("form.delete", "删除表单"),
    FORM_PUBLISH("form.publish", "发布/停止表单"),
    FORM_COPY("form.copy", "复制表单"),

    // 高级权限
    SUBMISSION_APPROVE("submission.approve", "审核记录"),
    SUBMISSION_ASSIGN("submission.assign", "分配记录");

    public static final Set<FormOperationPermission> ALL = Set.of(values());

    @JsonValue
    private final String code;
    private final String description;

    FormOperationPermission(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static FormOperationPermission fromCode(String code) {
        for (FormOperationPermission p : values()) {
            if (p.code.equals(code)) {
                return p;
            }
        }
        return null;
    }
}
