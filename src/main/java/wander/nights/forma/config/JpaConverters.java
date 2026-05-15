package wander.nights.forma.config;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import wander.nights.forma.shared.valueobject.FormId;
import wander.nights.forma.shared.valueobject.FormVersionId;

import java.util.UUID;

public abstract class JpaConverters {

    @Converter(autoApply = true)  // autoApply = true 表示自动应用于所有 Form.Id 类型字段
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

    @Converter(autoApply = true)  // autoApply = true 表示自动应用于所有 Form.Id 类型字段
    public static class FormVersionIdConverter implements AttributeConverter<FormVersionId, UUID> {
        @Override
        public UUID convertToDatabaseColumn(FormVersionId attribute) {
            return attribute == null ? null : attribute.value();
        }

        @Override
        public FormVersionId convertToEntityAttribute(UUID dbData) {
            return dbData == null ? null : new FormVersionId(dbData);
        }

    }
}
