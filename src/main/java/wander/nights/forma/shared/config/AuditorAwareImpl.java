package wander.nights.forma.shared.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import wander.nights.forma.shared.context.RequestContext;
import wander.nights.forma.shared.valueobject.UserId;

import java.util.Optional;

@Component
public class AuditorAwareImpl implements AuditorAware<UserId> {
    @NonNull
    @Override
    public Optional<UserId> getCurrentAuditor() {
        return Optional.ofNullable(RequestContext.currentUserId());
    }
}
