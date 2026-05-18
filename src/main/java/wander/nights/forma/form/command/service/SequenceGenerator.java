package wander.nights.forma.form.command.service;

import wander.nights.forma.shared.valueobject.FormId;

import java.util.List;

public interface SequenceGenerator {

    long nextSubmissionNo(FormId formId);

    // 批量获取多个编号
    List<Long> nextSubmissionNo(FormId formId, int count);
}
