package link.thingscloud.spring.boot.common.redis;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;


/**
 * <p>RedisTemplate0 class.</p>
 *
 * @author zhouhailin
 * @version 1.0.0
 */
@Slf4j
@Component
public class RedisTemplate0 extends AbstractRedisTemplate0 {

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
