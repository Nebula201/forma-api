package wander.nights.forma.event.outbox;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;

/**
 * 事件发件箱
 */
@Entity
@Table(name = "outbox_events")
@Data
public class OutboxEvent {

    /**
     * 事件Id
     */
    @Id
    @Column(name = "event_id")
    private java.util.UUID eventId;

    /**
     * 事件类型
     */
    @Column(name = "event_type", length = 64, nullable = false)
    private String eventType;

    /**
     * 事件版本
     */
    @Column(name = "event_version", nullable = false)
    private int eventVersion;

    /**
     * 事件发生时间
     */
    @Column(name = "event_at", nullable = false)
    private Instant eventAt;

    /**
     * 事件内容
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "payload", nullable = false)
    private String payload;

    /**
     * 发送状态
     */
    @Column(name = "status", length = 16, nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    /**
     * 发送时间
     */
    @Column(name = "published_at")
    private Instant publishedAt;

    /**
     * 最大重试次数
     */
    @Column(name = "max_retries", nullable = false)
    private int maxRetries = 3;

    /**
     * 已重试次数
     */
    @Column(name = "retry_count", nullable = false)
    private int retryCount = 0;

    /**
     * 失败原因
     */
    @Column(name = "failed_reason")
    private String failedReason;

    @Version
    @Column(name = "version")
    private long version = 0;

    public enum Status {
        PENDING, SENT, FAILED
    }
}
