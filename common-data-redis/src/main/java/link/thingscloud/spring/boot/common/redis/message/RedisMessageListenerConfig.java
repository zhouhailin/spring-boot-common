package link.thingscloud.spring.boot.common.redis.message;

import link.thingscloud.spring.boot.common.redis.message.RedisMessageListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

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

    @Autowired
    private List<RedisMessageListener> redisMessageListeners;

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
            log.info("redis message listener topic : {}, listener : {}", listener.getTopic(), listener.getClass().getName());
            container.addMessageListener(listener, new PatternTopic(listener.getTopic()));
        });
        return container;
    }


}
