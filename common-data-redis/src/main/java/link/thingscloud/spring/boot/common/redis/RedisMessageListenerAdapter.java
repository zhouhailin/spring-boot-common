package link.thingscloud.spring.boot.common.redis;

import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

/**
 * <p>Abstract RedisMessageListenerAdapter class.</p>
 *
 * @author zhouhailin
 * @version 1.0.0
 */
public abstract class RedisMessageListenerAdapter extends MessageListenerAdapter implements RedisMessageListener {

}
