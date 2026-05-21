package wander.nights.forma.form.query.dto.submission;

import lombok.Data;
import wander.nights.forma.shared.identifier.FieldCode;

import java.time.Instant;
import java.util.Map;

@Data
public class SubmissionVo {
    private String formId;
    private Integer submissionNo;
    private Integer formVersion;
    private Map<FieldCode, Object> content;
    private Instant submittedAt;
    private String deviceType;
    private String ipProvince;
    private String ipCity;
}
