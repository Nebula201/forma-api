package wander.nights.forma.shared.config;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import wander.nights.forma.shared.valueobject.FormId;
import wander.nights.forma.shared.valueobject.FormRoleCode;
import wander.nights.forma.shared.valueobject.UserId;


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
    public static class UserIdConverter implements AttributeConverter<UserId, String> {
        @Override
        public String convertToDatabaseColumn(UserId attribute) {
            return attribute == null ? null : attribute.value();
        }

        @Override
        public UserId convertToEntityAttribute(String dbData) {
            return dbData == null ? null : new UserId(dbData);
        }
    }
}
