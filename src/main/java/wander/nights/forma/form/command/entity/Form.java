package wander.nights.forma.form.command.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.type.SqlTypes;
import wander.nights.forma.model.FormSetting;
import wander.nights.forma.model.fields.FieldDefinition;
import wander.nights.forma.model.rule.RuleDefinition;
import wander.nights.forma.shared.valueobject.FormId;

import java.util.List;

/**
 * 表单主表
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "forms")
@Data
@SQLDelete(sql = "update forms set deleted_at = now(), version = version + 1 where form_id = ? and version = ?")
@SQLRestriction("deleted_at is null")
public class Form extends BaseEntity {
    /**
     * 表单Id
     */
    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "form_id"))
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

    /**
     * 表单状态
     */
    public enum Status {
        DRAFT,
        PUBLISHED,
        CLOSED
    }
}
