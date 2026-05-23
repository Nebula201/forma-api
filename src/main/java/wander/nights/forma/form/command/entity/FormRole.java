package wander.nights.forma.form.command.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.type.SqlTypes;
import wander.nights.forma.shared.config.JpaConverters;
import wander.nights.forma.shared.entity.BaseEntity;
import wander.nights.forma.shared.identifier.FormId;
import wander.nights.forma.shared.valueobject.FormRoleCode;

import java.util.Set;
import java.util.UUID;

/**
 * 表单角色
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "form_roles")
@Data
@SQLDelete(sql = "update form_roles set deleted_at = now(), version = version + 1 where form_role_id = ? and version = ?")
@SQLRestriction("deleted_at is null")
public class FormRole extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID formRoleId;

    /**
     * 表单Id
     */
    @Column(name = "form_id")
    @Convert(converter = JpaConverters.FormIdConverter.class)
    private FormId formId;
    /**
     * 角色编码
     */
    @Column(name = "role_code")
    @Convert(converter = JpaConverters.FormRoleCodeConverter.class)
    private FormRoleCode code;
    /**
     * 角色名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 角色描述
     */
    @Column(name = "description")
    private String description;

    /**
     * 操作权限
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "operation_permissions")
    private Set<FormOperationPermission> operationPermissions;

    /**
     * 访问权限
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "access_permissions")
    private FormAccessPermission accessPermissions = FormAccessPermission.UNLIMITED;

}
