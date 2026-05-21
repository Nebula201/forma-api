package wander.nights.forma.submission.service;

import wander.nights.forma.shared.identifier.FormId;

import java.util.List;

public interface SubmissionNoProvider {

    long nextSubmissionNo(FormId formId);

    // 批量获取多个编号
    List<Long> nextSubmissionNo(FormId formId, int count);
}
