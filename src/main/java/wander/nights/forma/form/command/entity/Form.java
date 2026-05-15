package wander.nights.forma.form.command.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import wander.nights.forma.model.FormSetting;
import wander.nights.forma.model.fields.FieldDefinition;
import wander.nights.forma.model.rule.RuleDefinition;
import wander.nights.forma.shared.valueobject.FormId;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * 表单主表
 */
@Entity
@Table(name = "forms")
@Data
@SQLDelete(sql = "UPDATE forms SET deleted_at = now() WHERE form_id = ?")  // 替换 DELETE 操作
@SQLRestriction("deleted_at is null")  // 自动过滤已删除的记录
public class Form {
    /**
     * 表单Id
     */
    @EmbeddedId
    private FormId formId;

    /**
     * 编码
     */
    @Column(name = "code", unique = true, length = 32)
    private String code;

    /**
     * 标题
     */
    @Column(name = "title")
    private String title;

    /**
     * 描述
     */
    @Column(name = "description")
    private String description;

    /**
     * 短Id
     */
    @Column(name = "short_id", unique = true, length = 16)
    private String shortId;

    /**
     * 表单状态
     */
    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private Status status;

    /**
     * 字段数组
     */
    @Column(name = "fields")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<FieldDefinition> fields;

    /**
     * 跳题逻辑
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "rules")
    private List<RuleDefinition> rules;

    /**
     * 表单设置
     */
    @Column(name = "settings")
    @JdbcTypeCode(SqlTypes.JSON)
    private FormSetting setting;

    @Column
    private UUID ownerId;

    /**
     * 创建时间
     */
    @CreatedDate
    @Column(name = "created_at")
    private Instant createdAt;

    /**
     * 创建人
     */
    @CreatedBy
    @Column(name = "created_by")
    private UUID createdBy;

    /**
     * 最近修改时间
     */
    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updatedAt;

    /**
     * 最近修改人
     */
    @LastModifiedBy
    private UUID updatedBy;

    /**
     * 删除时间
     */
    @Column(name = "deleted_at")
    private Instant deletedAt;

    /**
     * 表单状态
     */
    public enum Status {
        DRAFT,
        PUBLISHED,
        CLOSED
    }
}
