package wander.nights.forma.shared.exception;

import lombok.Getter;

@Getter
public class SystemException extends RuntimeException {
    private final int code;
    private final String detail;

    public SystemException(int code, String message) {
        super(message);
        this.code = code;
        this.detail = message;
    }

    public SystemException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.detail = message;
    }
}
