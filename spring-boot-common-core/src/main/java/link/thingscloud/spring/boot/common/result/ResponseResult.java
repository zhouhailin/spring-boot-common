package link.thingscloud.spring.boot.common.result;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author zhouhailin
 * @since 1.0.0
 */
@Data
@Accessors
public class ResponseResult<T> {
    public final int code;
    public final String message;
    public final T data;

    public static <T> ResponseResult<T> newResponse(int code, String message, T data) {
        return new ResponseResult<>(code, message, data);
    }
}
