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
    @AttributeOverride(name = "formId.value", column = @Column(name = "form_id"))
    @AttributeOverride(name = "submissionNo", column = @Column(name = "submission_no"))
    private FormSubmissionId formSubmissionId;

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

    /**
     * 二维码追踪码
     */
    @Column(name = "tracking_id")
    private String trackingId;

    /**
     * 浏览器 User-Agent
     */
    @Column(name = "ua")
    private String ua;

    /**
     * 来源渠道
     */
    @Column(name = "referrer")
    private String referrer;

    /**
     * 设备类型 手机/桌面设备/平板
     */
    @Column(name = "device_type")
    private String deviceType;

    /**
     * 设备指纹
     */
    @Column(name = "device_hash")
    private String deviceHash;

    /**
     * 国家
     */
    @Column(name = "ip_country", length = 50)
    private String ipCountry;
    /**
     * 省份
     */
    @Column(name = "ip_province", length = 50)
    private String ipProvince;

    /**
     * 城市
     */
    @Column(name = "ip_city", length = 50)
    private String ipCity;

    /**
     * 额外属性
     */
    @Column(name = "attributes")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> attributes;


    public FormId getFormId() {
        return getFormSubmissionId().formId();
    }
}
