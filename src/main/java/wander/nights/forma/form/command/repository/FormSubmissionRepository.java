package wander.nights.forma.form.command.repository;

import com.github.f4b6a3.uuid.alt.GUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wander.nights.forma.form.command.entity.FormSubmission;
import wander.nights.forma.shared.valueobject.FormSubmissionId;

@Repository
public interface FormSubmissionRepository extends JpaRepository<FormSubmission, FormSubmissionId> {
    default FormSubmissionId nextId() {
        return new FormSubmissionId(GUID.v7().toUUID());
    }

}
