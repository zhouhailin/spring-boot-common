package link.thingscloud.spring.boot.common.redis;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
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

    private static final int DEFAULT_TIMEOUT = 300_000;
    private static final String NS_LOCK = "link:thingscloud:redis:lock:";

    /**
     * <p>lock.</p>
     *
     * @param key      a {@link java.lang.String} object.
     * @param callback a {@link link.thingscloud.spring.boot.common.redis.RedisResponseCallback} object.
     */
    public void lock(String key, RedisResponseCallback callback) {
        lock(key, DEFAULT_TIMEOUT, callback);
    }

    /**
     * <p>lock.</p>
     *
     * @param key      a {@link java.lang.String} object.
     * @param timeout  a long.
     * @param callback a {@link link.thingscloud.spring.boot.common.redis.RedisResponseCallback} object.
     */
    public void lock(String key, long timeout, RedisResponseCallback callback) {
        Boolean lock;
        try {
            lock = stringRedisTemplate.opsForValue().setIfAbsent(NS_LOCK + key, DateUtil.now(), timeout, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            callback.onException(e);
            return;
        }
        if (lock != null && lock) {
            try {
                long start = System.currentTimeMillis();
                callback.onSucceed();
                long cost = System.currentTimeMillis() - start;
                if (cost >= timeout) {
                    // 存在风险
                    log.warn("[lock] key : {}, timeout : {} ms, cost : {} ms, you should increment timeout!", key, timeout, System.currentTimeMillis() - start);
                } else {
                    log.info("[lock] key : {}, timeout : {} ms, cost : {} ms", key, timeout, System.currentTimeMillis() - start);
                }
            } catch (Exception e) {
                callback.onException(e);
            } finally {
                stringRedisTemplate.delete(NS_LOCK + key);
            }
        } else {
            callback.onFailure();
        }
    }

    /**
     * <p>convertAndSend.</p>
     *
     * @param channel a {@link java.lang.String} object.
     * @param message a {@link java.lang.String} object.
     */
    public void convertAndSend(String channel, String message) {
        stringRedisTemplate.convertAndSend(channel, message);
    }

    /**
     * <p>opsForHash.</p>
     *
     * @return a {@link org.springframework.data.redis.core.HashOperations} object.
     */
    public HashOperations<String, String, String> opsForHash() {
        return stringRedisTemplate.opsForHash();
    }

    /**
     * <p>opsForValue.</p>
     *
     * @return a {@link org.springframework.data.redis.core.ValueOperations} object.
     */
    public ValueOperations<String, String> opsForValue() {
        return stringRedisTemplate.opsForValue();
    }
}
