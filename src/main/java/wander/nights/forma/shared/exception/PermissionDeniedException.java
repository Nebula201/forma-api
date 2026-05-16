package wander.nights.forma.shared.exception;

/**
 * 权限不足
 */
public class PermissionDeniedException extends BusinessException {
    public PermissionDeniedException(String action) {
        super(ErrorCode.PERMISSION_DENIED, "权限不足", "当前用户无权执行: " + action);
    }
}
