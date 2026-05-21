package wander.nights.forma.form.command.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wander.nights.forma.form.command.entity.FormCollaborator;
import wander.nights.forma.shared.identifier.FormId;
import wander.nights.forma.shared.identifier.OperatorId;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FormCollaboratorRepository extends JpaRepository<FormCollaborator, UUID> {

    List<FormCollaborator> findByFormId(FormId formId);

    Optional<FormCollaborator> findByFormIdAndUserId(FormId formId, OperatorId userId);
}
