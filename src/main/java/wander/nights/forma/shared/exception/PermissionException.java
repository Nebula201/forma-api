package wander.nights.forma.shared.exception;

import org.springframework.http.ProblemDetail;

public class PermissionException extends BusinessException {
    public PermissionException(String message) {
        super(message);
    }

    public ProblemDetail toProblemDetail() {
        ProblemDetail detail = ProblemDetail.forStatus(403);
        detail.setTitle("无权访问该资源");
        return detail;
    }
}
