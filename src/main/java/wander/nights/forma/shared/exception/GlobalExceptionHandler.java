package wander.nights.forma.shared.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ProblemDetail handleBusinessException(BusinessException ex) {
        return ex.toProblemDetail();
    }

    // 2. 参数校验异常（@Valid）
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, List<String>> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String field = (error instanceof FieldError)
                    ? ((FieldError) error).getField()
                    : error.getObjectName();
            String message = error.getDefaultMessage();
            errors.computeIfAbsent(field, k -> new java.util.ArrayList<>()).add(message);
        });

        ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        detail.setTitle("参数校验失败");
        detail.setDetail("请求参数不合法，请检查后重试");
        detail.setProperty("errors", errors);

        log.debug("Validation failed: {}", errors);
        return detail;
    }

    // 3. 单个参数校验异常（@RequestParam @PathVariable）
    @ExceptionHandler({ConstraintViolationException.class,
            MethodArgumentTypeMismatchException.class,
            MissingServletRequestParameterException.class})
    public ProblemDetail handleParameterException(Exception ex) {
        String message = switch (ex) {
            case ConstraintViolationException cve -> cve.getConstraintViolations().stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining("; "));
            case MethodArgumentTypeMismatchException mtme -> String.format("参数 '%s' 类型错误，期望类型: %s",
                    mtme.getName(), mtme.getRequiredType().getSimpleName());
            case MissingServletRequestParameterException mspe ->
                    String.format("缺少必要参数: %s", mspe.getParameterName());
            default -> ex.getMessage();
        };

        ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        detail.setTitle("参数错误");
        detail.setDetail(message);

        return detail;
    }

    // 4. JSON 解析异常
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleJsonParseException(HttpMessageNotReadableException ex) {
        ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        detail.setTitle("请求格式错误");
        detail.setDetail("请求体格式不正确，请检查 JSON 格式");
        detail.setProperty("traceId", "");

        log.debug("JSON parse error: {}", ex.getMessage());
        return detail;
    }
}
