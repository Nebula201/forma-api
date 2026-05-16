package wander.nights.forma.shared.exception;

/**
 * 操作不允许
 */
public class OperationNotAllowException extends BusinessException {
    public OperationNotAllowException(String detail) {
        super(ErrorCode.OPERATION_NOT_ALLOWED, "操作不允许", detail);
    }
}
