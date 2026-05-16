package wander.nights.forma.form.command.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import wander.nights.forma.shared.config.JpaConverters;
import wander.nights.forma.shared.valueobject.FormId;
import wander.nights.forma.shared.valueobject.FormRoleCode;
import wander.nights.forma.shared.valueobject.UserId;

import java.util.UUID;

/**
 * 表单协作者
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "form_collaborators")
@Data
@SQLDelete(sql = "update form_collaborators set deleted_at = now(), version = version + 1 where id = ? and version = ?")
@SQLRestriction("deleted_at is null")
public class FormCollaborator extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    /**
     * 表单Id
     */
    @Column(name = "form_id")
    @Convert(converter = JpaConverters.FormIdConverter.class)
    private FormId formId;
    /**
     * 用户Id
     */
    @Column(name = "user_id")
    @Convert(converter = JpaConverters.UserIdConverter.class)
    private UserId userId;

    /**
     * 角色编码
     */
    @Column(name = "role_code")
    private FormRoleCode roleCode;
}
