package wander.nights.forma.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import wander.nights.forma.shared.valueobject.FormId;

/**
 * Spring
 */
public abstract class SpringConverters {
    @Component
    public static class StringToFormIdConverter implements Converter<String, FormId> {
        @Override
        public FormId convert(String source) {
            // source 是 URL 中的字符串，如 "550e8400-e29b-41d4-a716-446655440000"
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
}
