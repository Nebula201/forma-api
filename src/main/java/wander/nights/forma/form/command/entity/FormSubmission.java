package wander.nights.forma.form.command.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import wander.nights.forma.shared.valueobject.FieldCode;
import wander.nights.forma.shared.valueobject.FormId;
import wander.nights.forma.shared.valueobject.FormSubmissionId;

import java.net.InetAddress;
import java.time.Instant;
import java.util.Map;

@Entity
@Table(name = "form_submissions")
@Data
@SQLDelete(sql = "UPDATE form_submissions SET deleted_at = now() WHERE submission_id = ?")  // 替换 DELETE 操作
@SQLRestriction("deleted_at is null")  // 自动过滤已删除的记录
public class FormSubmission {

    /**
     * 数据Id
     */
    @EmbeddedId
    private FormSubmissionId formSubmissionId;

    /**
     * 表单Id
     */
    @Column(name = "form_id")
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
    private InetAddress submittedIp;

    /**
     * 填写时长（秒）
     */
    @Column(name = "duration_second")
    private Integer durationSecond;

    @CreatedDate
    @Column(name = "created_at")
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updatedAt;

    /**
     * 删除时间
     */
    @Column(name = "deleted_at")
    private Instant deletedAt;
}
