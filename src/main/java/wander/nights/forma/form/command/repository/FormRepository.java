package wander.nights.forma.form.command.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wander.nights.forma.form.command.entity.Form;
import wander.nights.forma.shared.valueobject.FormId;

@Repository
public interface FormRepository extends JpaRepository<Form, FormId> {
}
