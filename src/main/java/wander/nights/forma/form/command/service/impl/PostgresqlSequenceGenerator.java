package wander.nights.forma.form.command.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.hibernate.validator.constraints.Range;
import org.springframework.stereotype.Component;
import wander.nights.forma.form.command.service.SequenceGenerator;
import wander.nights.forma.shared.valueobject.FormId;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@Component
public class PostgresqlSequenceGenerator implements SequenceGenerator {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public long nextSubmissionNo(FormId formId) {
        return nextSubmissionNo(formId, 1).getFirst();
    }

    @Override
    public List<Long> nextSubmissionNo(FormId formId, @Range(min = 1, max = 10000) int count) {
        String sql = """
                INSERT INTO form_submission_sequence(form_id, current_no)
                VALUES (:formId, :count)
                ON CONFLICT(form_id)
                DO UPDATE SET current_no = form_submission_sequence.current_no + :count
                RETURNING current_no;
                """;

        Query query = entityManager.createNativeQuery(sql)
                .setParameter("formId", formId.value())
                .setParameter("count", count);

        Number newCurrentNo = (Number) query.getSingleResult();
        long startNo = newCurrentNo.longValue() - count + 1;

        return LongStream.range(startNo, startNo + count)
                .boxed()
                .collect(Collectors.toList());
    }
}
