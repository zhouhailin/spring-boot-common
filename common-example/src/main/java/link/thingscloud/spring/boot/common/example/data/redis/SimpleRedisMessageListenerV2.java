package link.thingscloud.spring.boot.common.example.data.redis;

import link.thingscloud.spring.boot.common.redis.RedisMessageListenerAdapter;
import link.thingscloud.spring.boot.common.redis.SimpleRedisTemplate;
import link.thingscloud.spring.boot.common.redis.annotation.RedisTopic;
import link.thingscloud.spring.boot.common.util.ExecutorHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * <p>SimpleRedisMessageListener class.</p>
 *
 * @author zhouhailin
 * @version 1.0.0
 */
@Slf4j
@RedisTopic("topic-SimpleRedisMessageListenerV2")
@Component
public class SimpleRedisMessageListenerV2 extends RedisMessageListenerAdapter {

    @Autowired
    private SimpleRedisTemplate simpleRedisTemplate;

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleMessage(String message, String channel) {
        log.info("handleMessage channel : {}, message : {}", channel, message);
    }

    /**
     * <p>startup.</p>
     */
    @PostConstruct
    public void startup() {
        ExecutorHelper.schedule(() -> {
            log.info("schedule ...");
            simpleRedisTemplate.convertAndSend(getClass().getAnnotation(RedisTopic.class).value(), "Hello");
        }, 5L, TimeUnit.SECONDS);
    }
}
