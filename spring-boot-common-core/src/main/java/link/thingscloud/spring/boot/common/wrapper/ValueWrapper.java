package link.thingscloud.spring.boot.common.wrapper;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author zhouhailin
 * @since 1.1.0
 */
@Data
@Accessors(chain = true)
public class ValueWrapper<T> {
    private T value;
}
