package wander.nights.forma.submission.service;

import wander.nights.forma.form.query.dto.SubmissionQuery;
import wander.nights.forma.form.query.dto.submission.SubmissionVo;
import wander.nights.forma.shared.identifier.FormId;

import java.util.List;

public interface FormSubmissionReadService {

    List<SubmissionVo> query(FormId formId, SubmissionQuery query);
}
