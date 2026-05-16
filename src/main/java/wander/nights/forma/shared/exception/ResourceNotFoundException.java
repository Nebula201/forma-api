package wander.nights.forma.shared.exception;

/**
 * 资源不存在
 */
public class ResourceNotFoundException extends BusinessException {
    public ResourceNotFoundException(String resourceName, Object id) {
        super(ErrorCode.RESOURCE_NOT_FOUND, resourceName + "不存在",
                String.format("%s[id=%s] 不存在", resourceName, id));
    }
}
