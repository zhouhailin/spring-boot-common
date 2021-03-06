package link.thingscloud.spring.boot.common.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Redis操作 API
 *
 * @author zhouhailin
 * @version 1.1.0
 */
@Slf4j
@Component
public class SimpleRedisTemplate {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public Boolean setIfAbsent(String key, String value, long timeoutMillis) {
        return stringRedisTemplate.opsForValue().setIfAbsent(key, value, timeoutMillis, TimeUnit.MILLISECONDS);
    }

    /**
     * 消息队列 - 发布消息
     *
     * @param channel a {@link java.lang.String} object.
     * @param message a {@link java.lang.String} object.
     */
    public void convertAndSend(String channel, String message) {
        stringRedisTemplate.convertAndSend(channel, message);
    }

    /**
     * 获取hash数据结构操作对象
     *
     * @return a {@link org.springframework.data.redis.core.HashOperations} object.
     */
    public HashOperations<String, String, String> opsForHash() {
        return stringRedisTemplate.opsForHash();
    }

    /**
     * 获取简单Value数据结构操作对象
     *
     * @return a {@link org.springframework.data.redis.core.ValueOperations} object.
     */
    public ValueOperations<String, String> opsForValue() {
        return stringRedisTemplate.opsForValue();
    }

    public ListOperations<String, String> opsForList() {
        return stringRedisTemplate.opsForList();
    }

}
