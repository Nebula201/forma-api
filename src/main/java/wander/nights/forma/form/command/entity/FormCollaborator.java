package wander.nights.forma.form.command.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import wander.nights.forma.shared.valueobject.FormId;
import wander.nights.forma.shared.valueobject.UserId;

import java.time.Instant;
import java.util.UUID;

/**
 * 表单协作者
 */
@Entity
@Table(name = "form_collaborators")
@Data
public class FormCollaborator {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    /**
     * 表单Id
     */
    @Embedded
    @Column(name = "form_id")
    private FormId formId;
    /**
     * 用户Id
     */
    @Column(name = "user_id")
    private UserId userId;

    /**
     * 角色编码
     */
    @Column(name = "role_code")
    private String roleCode;

    @CreatedDate
    @Column(name = "created_at")
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updatedAt;
}
