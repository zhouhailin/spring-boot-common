package link.thingscloud.spring.boot.common.redis.exception;

import io.lettuce.core.RedisException;

/**
 * <p>RedisLockException class.</p>
 *
 * @author zhouhailin
 * @version $Id: $Id
 */
public class RedisLockException extends RedisException {
    /**
     * <p>Constructor for RedisLockException.</p>
     *
     * @param cause a {@link java.lang.Throwable} object.
     */
    public RedisLockException(Throwable cause) {
        super(cause);
    }
}
