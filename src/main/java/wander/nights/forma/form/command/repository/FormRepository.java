package wander.nights.forma.form.command.repository;

import com.github.f4b6a3.uuid.alt.GUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wander.nights.forma.form.command.entity.Form;
import wander.nights.forma.shared.valueobject.FormId;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface FormRepository extends JpaRepository<Form, FormId> {
    default FormId nextId() {
        return new FormId(GUID.v7().toUUID());
    }

    Optional<Form> findByCode(String code);

    Optional<Form> findByShortId(String shortId);

    Page<Form> findByStatus(Form.Status status, Pageable pageable);

    Page<Form> findByOwnerId(UUID ownerId, Pageable pageable);

    Page<Form> findByStatusAndOwnerId(String status, UUID ownerId, Pageable pageable);

    boolean existsByCode(String code);

    boolean existsByShortId(String shortId);

    Page<Form> findByDeletedAtIsNull(Pageable pageable);
}
