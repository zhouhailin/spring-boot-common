package link.thingscloud.spring.boot.common.redis.callback;


import link.thingscloud.spring.boot.common.callback.ResponseCallback;
import link.thingscloud.spring.boot.common.redis.exception.RedisLockerException;

/**
 * <p>RedisResponseCallback interface.</p>
 *
 * @author zhouhailin
 * @version 1.0.0
 */
public interface RedisResponseCallback extends ResponseCallback {

    /**
     * <p>onException.</p>
     *
     * @param cause a {@link java.lang.Throwable} object.
     */
    @Override
    default void onException(Throwable cause) {
        throw new RedisLockerException(cause);
    }

}
