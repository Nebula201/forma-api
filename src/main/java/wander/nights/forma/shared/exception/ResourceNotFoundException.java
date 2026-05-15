package wander.nights.forma.shared.exception;

import org.springframework.http.ProblemDetail;

public class ResourceNotFoundException extends BusinessException {
    public ResourceNotFoundException(String message) {
        super(message);
    }

    @Override
    public ProblemDetail toProblemDetail() {
        ProblemDetail detail = ProblemDetail.forStatus(404);
        detail.setTitle("资源不存在");
        return detail;
    }
}
