package wander.nights.forma.shared.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import wander.nights.forma.shared.context.RequestContext;
import wander.nights.forma.shared.identifier.OperatorId;

import java.time.Instant;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {
    /**
     * 创建人ID
     */
    @CreatedBy
    @AttributeOverride(name = "value", column = @Column(name = "created_by", updatable = false))
    private OperatorId createdBy;

    /**
     * 创建时间
     */
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    /**
     * 最后修改人ID
     */
    @LastModifiedBy
    @AttributeOverride(name = "value", column = @Column(name = "updated_by"))
    private OperatorId updatedBy;

    /**
     * 最后修改时间
     */
    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updatedAt;

    /**
     * 最后修改IP
     */
    @Column(name = "updated_ip")
    @JdbcTypeCode(SqlTypes.INET)
    private String updatedIp;

    // 软删除标记
    @Column(name = "deleted_at")
    private Instant deletedAt;

    /**
     * 乐观锁
     */
    @Version
    @Column(name = "version")
    private int version = 0;


    @PreUpdate
    public void preUpdate() {
        fillIp();
    }

    @PrePersist
    public void prePersist() {
        fillIp();
    }

    private void fillIp() {
        var env = RequestContext.env();
        if (env != null && env.ip() != null) {
            this.updatedIp = env.ip().getHostAddress();
        }
    }
}
