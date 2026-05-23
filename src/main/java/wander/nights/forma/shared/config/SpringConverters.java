package wander.nights.forma.shared.config;

import org.jspecify.annotations.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import wander.nights.forma.shared.identifier.FormId;
import wander.nights.forma.shared.identifier.OperatorId;
import wander.nights.forma.shared.valueobject.FormRoleCode;

public abstract class SpringConverters {
    @Component
    public static class StringToFormIdConverter implements Converter<String, FormId> {
        @Override
        public FormId convert(String source) {
            if (source.isBlank()) {
                throw new IllegalArgumentException("FormId cannot be empty");
            }

            try {
                return FormId.of(source);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid FormId format: " + source, e);
            }
        }
    }

    @Component
    public static class StringToOperatorIdConverter implements Converter<String, OperatorId> {
        @Override
        public OperatorId convert(String source) {
            if (source.isBlank()) {
                throw new IllegalArgumentException("OperatorId cannot be empty");
            }
            return OperatorId.of(source);
        }
    }

    @Component
    public static class StringToFormRoleCodeConverter implements Converter<String, FormRoleCode> {
        @Override
        public FormRoleCode convert(@NonNull String source) {
            return new FormRoleCode(source);

        }
    }
}
