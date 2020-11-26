package link.thingscloud.spring.boot.common.redis.callback;


import link.thingscloud.spring.boot.common.redis.exception.RedisLockException;

/**
 * <p>RedisResponseCallback interface.</p>
 *
 * @author zhouhailin
 * @version 1.0.0
 */
public interface RedisResponseCallback {

    /**
     * <p>onSucceed.</p>
     */
    void onSucceed();

    /**
     * <p>onFailure.</p>
     */
    default void onFailure() {
    }

    /**
     * <p>onException.</p>
     *
     * @param cause a {@link java.lang.Throwable} object.
     */
    default void onException(Throwable cause) {
        throw new RedisLockException(cause);
    }

}
