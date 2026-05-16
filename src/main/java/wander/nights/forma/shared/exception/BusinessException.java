package wander.nights.forma.shared.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final int code;      // 业务错误码
    private final String detail;

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
        this.detail = message;
    }

    public BusinessException(int code, String message, String detail) {
        super(message);
        this.code = code;
        this.detail = detail;
    }

    public BusinessException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.detail = message;
    }
}
