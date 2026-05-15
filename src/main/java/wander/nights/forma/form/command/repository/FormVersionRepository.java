package wander.nights.forma.form.command.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wander.nights.forma.form.command.entity.FormVersion;
import wander.nights.forma.shared.valueobject.FormId;
import wander.nights.forma.shared.valueobject.FormVersionId;

import java.util.Optional;

@Repository
public interface FormVersionRepository extends JpaRepository<FormVersion, FormVersionId> {

    Optional<FormVersion> findByFormIdAndFormVersion(FormId formId, Integer formVersion);
}
