package wander.nights.forma.form.command.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import wander.nights.forma.model.FormContent;
import wander.nights.forma.shared.valueobject.FormId;
import wander.nights.forma.shared.valueobject.FormVersionId;

import java.net.InetAddress;
import java.time.Instant;
import java.util.UUID;

/**
 * 表单版本表
 *
 * <p>用途：记录表单发布后的历史快照，以及该版本对应的数据存储表名。
 *
 * <p>说明：
 * <ul>
 *   <li>每次表单发布操作会生成一个新的版本记录</li>
 *   <li>快照内容保存表单发布时的完整结构定义</li>
 * </ul>
 *
 * @author Wander Nights
 * @since 1.0.0
 */
@Table(name = "form_versions")
@Entity
@Data
public class FormVersion {
    /**
     * Id
     */
    @EmbeddedId
    private FormVersionId formVersionId;

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
     * 表单内容
     */
    @Column(name = "form_content")
    @JdbcTypeCode(SqlTypes.JSON)
    private FormContent formContent;

    /**
     * 发布人
     */
    @Column(name = "published_by")
    private UUID publishedBy;

    /**
     * 发布时间
     */
    @Column(name = "published_at")
    private Instant publishedAt;

    /**
     * 发布ip
     */
    @Column(name = "published_ip")
    private InetAddress publishedIp;
}
