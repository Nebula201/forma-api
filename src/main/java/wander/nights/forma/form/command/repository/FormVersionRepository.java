package wander.nights.forma.form.command.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wander.nights.forma.form.command.entity.FormVersion;
import wander.nights.forma.shared.identifier.FormId;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface FormVersionRepository extends JpaRepository<FormVersion, UUID> {

    Optional<FormVersion> findByFormIdAndFormVersion(FormId formId, Integer formVersion);
}
