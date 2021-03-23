package link.thingscloud.spring.boot.common.redis.message;

import link.thingscloud.spring.boot.common.redis.annotation.RedisTopic;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import java.util.Collections;
import java.util.List;

/**
 * <p>RedisMessageListenerConfig class.</p>
 *
 * @author zhouhailin
 * @version 1.0.0
 */
@Slf4j
@Configuration
public class RedisMessageListenerConfig {

    @Autowired(required = false)
    private final List<RedisMessageListener> redisMessageListeners = Collections.emptyList();

    /**
     * <p>redisMessageListenerContainer.</p>
     *
     * @param connectionFactory a {@link org.springframework.data.redis.connection.RedisConnectionFactory} object.
     * @return a {@link org.springframework.data.redis.listener.RedisMessageListenerContainer} object.
     */
    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        // 订阅频道
        redisMessageListeners.forEach(listener -> {
            String topic = listener.getTopic();
            if (topic == null) {
                RedisTopic redisTopic = listener.getClass().getAnnotation(RedisTopic.class);
                if (redisTopic == null) {
                    redisTopic = listener.getClass().getSuperclass().getAnnotation(RedisTopic.class);
                }
                if (redisTopic != null) {
                    topic = redisTopic.value();
                }
            }
            if (topic == null) {
                log.warn("redis message listener {}, topic is null, please use RedisTopic Annotation or implementation getTopic method", listener.getClass().getName());
                return;
            }
            log.info("redis message listener topic : {}, listener : {}", topic, listener.getClass().getName());
            container.addMessageListener(listener, new PatternTopic(topic));
        });
        return container;
    }


}
