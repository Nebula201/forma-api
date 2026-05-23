package wander.nights.forma.shared.config;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import wander.nights.forma.shared.identifier.FormId;
import wander.nights.forma.shared.identifier.OperatorId;
import wander.nights.forma.shared.valueobject.FormRoleCode;


import java.util.UUID;

public abstract class JpaConverters {

    @Converter(autoApply = true)
    public static class FormIdConverter implements AttributeConverter<FormId, UUID> {
        @Override
        public UUID convertToDatabaseColumn(FormId attribute) {
            return attribute == null ? null : attribute.value();
        }

        @Override
        public FormId convertToEntityAttribute(UUID dbData) {
            return dbData == null ? null : new FormId(dbData);
        }
    }

    @Converter(autoApply = true)
    public static class FormRoleCodeConverter implements AttributeConverter<FormRoleCode, String> {
        @Override
        public String convertToDatabaseColumn(FormRoleCode attribute) {
            return attribute == null ? null : attribute.value();
        }

        @Override
        public FormRoleCode convertToEntityAttribute(String dbData) {
            return dbData == null ? null : new FormRoleCode(dbData);
        }
    }

    @Converter(autoApply = true)
    public static class OperatorIdConverter implements AttributeConverter<OperatorId, String> {
        @Override
        public String convertToDatabaseColumn(OperatorId attribute) {
            return attribute == null ? null : attribute.value();
        }

        @Override
        public OperatorId convertToEntityAttribute(String dbData) {
            return dbData == null ? null : OperatorId.of(dbData);
        }
    }
}
