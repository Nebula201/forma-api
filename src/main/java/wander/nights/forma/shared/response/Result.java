package wander.nights.forma.shared.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.ProblemDetail;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class Result<T> {
    /**
     * 响应码，200表示成功，其他表示错误
     */
    private int code;
    /**
     * 响应消息，通常是操作结果的描述
     */
    private String message;
    /**
     * 返回的实际数据
     */
    private T data;
    /**
     * 错误详情，失败时返回
     */
    private ProblemDetail error;

    public boolean isSuccess() {
        return code == 200;
    }

    public static <T> Result<T> ok(T data) {
        return new Result<>(200, "", data, null);
    }

    public static Result<?> error(ProblemDetail detail) {
        return new Result<>(500, "", null, detail);
    }


}
