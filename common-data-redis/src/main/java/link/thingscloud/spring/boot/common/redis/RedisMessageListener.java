package link.thingscloud.spring.boot.common.redis;

import org.springframework.data.redis.connection.MessageListener;

/**
 * <p>RedisMessageListener interface.</p>
 *
 * @author zhouhailin
 * @version 1.0.0
 */
public interface RedisMessageListener extends MessageListener {

    /**
     * <p>getTopic.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    String getTopic();

    /**
     * <p>handleMessage.</p>
     *
     * @param message a {@link java.lang.String} object.
     * @param channel a {@link java.lang.String} object.
     */
    void handleMessage(String message, String channel);

}
