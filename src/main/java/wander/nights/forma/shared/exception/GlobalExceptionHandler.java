package wander.nights.forma.shared.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.net.URI;
import java.time.Instant;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    // ==================== 业务异常 ====================

    @ExceptionHandler(BusinessException.class)
    public ProblemDetail handleBusinessException(BusinessException e, HttpServletRequest request) {
        log.warn("业务异常: code={}, message={}", e.getCode(), e.getMessage());
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                e.getDetail() != null ? e.getDetail() : e.getMessage()
        );
        problemDetail.setTitle("业务异常");
        problemDetail.setType(URI.create("https://api.example.com/errors/business"));
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setProperty("code", e.getCode());
        problemDetail.setProperty("timestamp", Instant.now().toEpochMilli());

        return problemDetail;
    }

    // ==================== 参数校验异常 ====================
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationException(MethodArgumentNotValidException e, HttpServletRequest request) {
        String errorMsg = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));
        log.warn("参数校验失败: {}", errorMsg);
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, errorMsg);
        problemDetail.setTitle("参数校验失败");
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setProperty("code", ErrorCode.INVALID_PARAMETER);
        problemDetail.setProperty("timestamp", Instant.now().toEpochMilli());

        // 可选：返回详细字段错误
        problemDetail.setProperty("errors", e.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField,
                        error -> StringUtils.defaultString(error.getDefaultMessage()))));

        return problemDetail;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleConstraintViolation(ConstraintViolationException e,
                                                   HttpServletRequest request) {
        String errorMsg = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining("; "));

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                errorMsg
        );
        problemDetail.setTitle("参数约束违反");
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setProperty("code", ErrorCode.INVALID_PARAMETER);
        problemDetail.setProperty("timestamp", Instant.now().toEpochMilli());

        return problemDetail;
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ProblemDetail handleMissingParam(MissingServletRequestParameterException e,
                                            HttpServletRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "缺少必要参数: " + e.getParameterName()
        );
        problemDetail.setTitle("参数缺失");
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setProperty("code", ErrorCode.BAD_REQUEST);
        problemDetail.setProperty("timestamp", Instant.now().toEpochMilli());

        return problemDetail;
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ProblemDetail handleTypeMismatch(MethodArgumentTypeMismatchException e,
                                            HttpServletRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                String.format("参数 %s 类型错误，期望类型: %s",
                        e.getName(), e.getRequiredType().getSimpleName())
        );
        problemDetail.setTitle("参数类型错误");
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setProperty("code", ErrorCode.INVALID_PARAMETER);
        problemDetail.setProperty("timestamp", Instant.now().toEpochMilli());

        return problemDetail;
    }

    // ==================== 系统异常 ====================

    @ExceptionHandler(SystemException.class)
    public ProblemDetail handleSystemException(SystemException e, HttpServletRequest request) {
        log.error("系统异常: code={}, message={}", e.getCode(), e.getMessage(), e);

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "系统内部错误，请稍后重试"  // 对用户友好的提示
        );
        problemDetail.setTitle("系统异常");
        problemDetail.setType(URI.create("https://api.example.com/errors/system"));
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setProperty("code", e.getCode());
        problemDetail.setProperty("timestamp", Instant.now().toEpochMilli());

        // 开发环境可返回详细错误（可选）
        if (isDevEnvironment()) {
            problemDetail.setProperty("debug_detail", e.getDetail());
        }

        return problemDetail;
    }

    // ==================== 兜底异常 ====================

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGenericException(Exception e, HttpServletRequest request) {
        log.error("未捕获的异常: {}", e.getMessage(), e);

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "服务繁忙，请稍后重试"  // 不暴露内部细节
        );
        problemDetail.setTitle("服务内部错误");
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setProperty("code", ErrorCode.INTERNAL_ERROR);
        problemDetail.setProperty("timestamp", Instant.now().toEpochMilli());

        return problemDetail;
    }


    // ==================== 辅助方法 ====================

    private boolean isDevEnvironment() {
        // 根据 spring.profiles.active 判断
//        return Arrays.asList(environment.getActiveProfiles()).contains("dev");
        return false;
    }
}
