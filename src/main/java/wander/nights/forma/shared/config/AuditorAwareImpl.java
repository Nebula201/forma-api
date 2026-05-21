package wander.nights.forma.shared.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import wander.nights.forma.shared.context.RequestContext;
import wander.nights.forma.shared.identifier.OperatorId;

import java.util.Optional;

@Component
public class AuditorAwareImpl implements AuditorAware<OperatorId> {
    @NonNull
    @Override
    public Optional<OperatorId> getCurrentAuditor() {
        return Optional.ofNullable(RequestContext.currentOperatorId());
    }
}
