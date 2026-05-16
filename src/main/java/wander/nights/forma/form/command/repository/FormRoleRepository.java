package wander.nights.forma.form.command.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wander.nights.forma.form.command.entity.FormRole;
import wander.nights.forma.shared.valueobject.FormId;
import wander.nights.forma.shared.valueobject.FormRoleCode;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FormRoleRepository extends JpaRepository<FormRole, UUID> {

    List<FormRole> findByFormId(FormId formId);

    Optional<FormRole> findByFormIdAndCode(FormId formId, FormRoleCode code);
}
