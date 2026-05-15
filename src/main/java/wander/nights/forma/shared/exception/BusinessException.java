package wander.nights.forma.shared.exception;

import org.springframework.http.ProblemDetail;

public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }

    public ProblemDetail toProblemDetail() {
        ProblemDetail detail = ProblemDetail.forStatus(500);
        detail.setTitle("系统异常");
        detail.setDetail(getMessage());
        return detail;
    }
}
