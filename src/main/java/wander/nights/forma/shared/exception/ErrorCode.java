package wander.nights.forma.shared.exception;

public interface ErrorCode {
    // 通用错误 1xxx
    int SUCCESS = 0;
    int BAD_REQUEST = 1000;
    int RESOURCE_NOT_FOUND = 1001;
    int DUPLICATE_KEY = 1002;
    int PERMISSION_DENIED = 1003;
    int INVALID_PARAMETER = 1004;
    int OPERATION_NOT_ALLOWED = 1005;

    // 用户模块 2xxx
    int USER_NOT_FOUND = 2001;
    int USER_DISABLED = 2002;
    int PASSWORD_ERROR = 2003;

    // 表单模块 3xxx
    int FORM_NOT_FOUND = 3001;
    int FORM_ALREADY_PUBLISHED = 3002;

    // 系统错误 9xxx
    int DB_ERROR = 9001;
    int THIRD_PARTY_ERROR = 9002;
    int INTERNAL_ERROR = 9999;
}