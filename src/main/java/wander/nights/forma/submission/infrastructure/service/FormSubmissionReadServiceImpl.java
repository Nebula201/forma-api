package wander.nights.forma.submission.infrastructure.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import wander.nights.forma.form.command.entity.Form;
import wander.nights.forma.form.command.repository.FormRepository;
import wander.nights.forma.form.query.dto.SubmissionQuery;
import wander.nights.forma.form.query.dto.submission.SubmissionVo;
import wander.nights.forma.model.fields.FieldDefinition;
import wander.nights.forma.shared.condition.ConditionExpression;
import wander.nights.forma.shared.exception.ResourceNotFoundException;
import wander.nights.forma.shared.identifier.FieldCode;
import wander.nights.forma.shared.identifier.FormId;
import wander.nights.forma.submission.service.FormSubmissionReadService;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class FormSubmissionReadServiceImpl implements FormSubmissionReadService {

    private final JdbcTemplate jdbcTemplate;
    private final FormRepository formRepository;
    private final ObjectMapper objectMapper;

    @Override
    public List<SubmissionVo> query(FormId formId, SubmissionQuery query) {
        Form form = formRepository.findById(formId)
                .orElseThrow(() -> new ResourceNotFoundException("Form", formId));

        Map<FieldCode, FieldDefinition> fieldDefMap = form.getFields().stream()
                .collect(Collectors.toMap(FieldDefinition::getCode, Function.identity()));

        Set<String> allowedFields = form.getFields().stream()
                .map(f -> f.getCode().value())
                .collect(Collectors.toSet());

        SqlGenerateVisitor visitor = new SqlGenerateVisitor(allowedFields, fieldDefMap);

        ConditionExpression filter = query.getFilter();
        if (filter == null) {
            String sql = "SELECT * FROM form_submissions WHERE form_id = ? AND deleted_at IS NULL ORDER BY submitted_at DESC";
            return jdbcTemplate.query(sql, this::mapRow, formId.value().toString());
        }

        SqlFragment fragment = filter.accept(visitor);
        String sql = "SELECT * FROM form_submissions WHERE form_id = ? AND deleted_at IS NULL AND " + fragment.sql()
                + " ORDER BY submitted_at DESC";

        List<Object> params = new ArrayList<>();
        params.add(formId.value().toString());
        params.addAll(fragment.params());
        return jdbcTemplate.query(sql, this::mapRow, params.toArray());
    }

    private SubmissionVo mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
        SubmissionVo vo = new SubmissionVo();
        vo.setFormId(rs.getString("form_id"));
        vo.setSubmissionNo(rs.getInt("submission_no"));
        vo.setFormVersion(rs.getInt("form_version"));
        vo.setSubmittedAt(rs.getTimestamp("submitted_at") != null ? rs.getTimestamp("submitted_at").toInstant() : null);
        vo.setDeviceType(rs.getString("device_type"));
        vo.setIpProvince(rs.getString("ip_province"));
        vo.setIpCity(rs.getString("ip_city"));

        String contentJson = rs.getString("content");
        if (contentJson != null) {
            try {
                Map<FieldCode, Object> content = objectMapper.readValue(contentJson,
                        new TypeReference<>() {
                        });
                vo.setContent(content);
            } catch (Exception e) {
                log.warn("Failed to parse submission content JSON: {}", e.getMessage());
            }
        }
        return vo;
    }
}
