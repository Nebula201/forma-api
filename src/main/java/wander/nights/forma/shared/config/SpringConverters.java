package wander.nights.forma.shared.config;

import org.jspecify.annotations.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import wander.nights.forma.shared.valueobject.FormId;
import wander.nights.forma.shared.valueobject.FormRoleCode;
import wander.nights.forma.shared.valueobject.UserId;

/**
 * Spring
 */
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
    public static class StringToUserIdConverter implements Converter<String, UserId> {
        @Override
        public UserId convert(String source) {
            if (source.isBlank()) {
                throw new IllegalArgumentException("UserId cannot be empty");
            }

            try {
                return UserId.of(source);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid UserId format: " + source, e);
            }
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
