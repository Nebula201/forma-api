package wander.nights.forma.form.command.entity;

import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.type.SqlTypes;
import wander.nights.forma.shared.config.JpaConverters;
import wander.nights.forma.shared.entity.BaseEntity;
import wander.nights.forma.shared.identifier.FormId;
import wander.nights.forma.shared.valueobject.FormRoleCode;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * 表单角色
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "form_roles")
@Data
@SQLDelete(sql = "update form_roles set deleted_at = now(), version = version + 1 where form_role_id = ? and version = ?")
@SQLRestriction("deleted_at is null")
public class FormRole extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID formRoleId;

    /**
     * 表单Id
     */
    @Column(name = "form_id")
    @Convert(converter = JpaConverters.FormIdConverter.class)
    private FormId formId;
    /**
     * 角色编码
     */
    @Column(name = "role_code")
    @Convert(converter = JpaConverters.FormRoleCodeConverter.class)
    private FormRoleCode code;
    /**
     * 角色名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 角色描述
     */
    @Column(name = "description")
    private String description;

    /**
     * 操作权限
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "operation_permissions")
    private Set<OperationPermission> operationPermissions;

    /**
     * 访问权限
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "access_permissions")
    private AccessPermission accessPermissions;


    // ==================== 内部类 ====================

    /**
     * 操作权限枚举
     */
    @Getter
    public enum OperationPermission {
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

        public static final Set<OperationPermission> ALL = Set.of(values());

        @JsonValue
        private final String code;
        private final String description;

        OperationPermission(String code, String description) {
            this.code = code;
            this.description = description;
        }

        public static OperationPermission fromCode(String code) {
            for (OperationPermission p : values()) {
                if (p.code.equals(code)) {
                    return p;
                }
            }
            return null;
        }
    }

    @Data
    public static class AccessPermission {
        /**
         * 字段级权限配置
         */
        private List<String> fields;

        /**
         * 记录级权限配置（行级过滤）
         */
        private List<RecordFilterCondition> recordFilters;
    }

    /**
     * 记录过滤条件
     */
    @Data
    public static class RecordFilterCondition {
        private String field;     // 字段名
        private String operator;      // 操作符：eq, ne, gt, lt, like, in
        private Object value;         // 值
    }
}
