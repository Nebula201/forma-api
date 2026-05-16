package wander.nights.forma.form.command.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.type.SqlTypes;
import wander.nights.forma.shared.config.JpaConverters;
import wander.nights.forma.shared.valueobject.FieldCode;
import wander.nights.forma.shared.valueobject.FormId;
import wander.nights.forma.shared.valueobject.FormSubmissionId;

import java.net.InetAddress;
import java.time.Instant;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "form_submissions")
@Data
@SQLDelete(sql = "update form_submissions set deleted_at = now(), version = version + 1 where submission_id = ? and version = ?")
@SQLRestriction("deleted_at is null")  // 自动过滤已删除的记录
public class FormSubmission extends BaseEntity {

    /**
     * 数据Id
     */
    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "submission_id"))
    private FormSubmissionId formSubmissionId;

    /**
     * 表单Id
     */
    @Column(name = "form_id")
    @Convert(converter = JpaConverters.FormIdConverter.class)
    private FormId formId;

    /**
     * 表单版本
     */
    @Column(name = "form_version")
    private Integer formVersion;

    /**
     * 原始数据内容
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "content")
    private Map<FieldCode, Object> content;

    /**
     * 提交时间
     */
    @Column(name = "submitted_at")
    private Instant submittedAt;

    /**
     * 提交Ip
     */
    @Column(name = "submitted_ip")
    @JdbcTypeCode(SqlTypes.INET)
    private InetAddress submittedIp;

    /**
     * 填写时长（秒）
     */
    @Column(name = "duration_second")
    private Integer durationSecond;

}
