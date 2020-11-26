package link.thingscloud.spring.boot.common.redis;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.ThreadUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author zhouhailin
 * @version 1.1.0
 */
@Slf4j
@Component
public class DistributedLock {

    @Value("${link.thingscloud.redis.distributed.lock.sleepTimeout:10}")
    private long sleepTimeoutMillis;
    @Value("${link.thingscloud.redis.distributed.lock.lockTimeout:1000}")
    private long lockTimeoutMillis;

    @Value("${link.thingscloud.redis.distributed.lock.expiredTimeout:300000}")
    private long expiredTimeoutMillis;

    @Value("${link.thingscloud.redis.distributed.lock.key:link:thingscloud:redis:distributed:lock:}")
    private String lockKey;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * Redis分布式锁 - 轻量级（尝试锁一次）
     * <p>
     * 在主从同步时，key未同步成功时，主挂，从为主时，会出现锁两次的情况
     *
     * @param key      a {@link java.lang.String} key值
     * @param callback a {@link link.thingscloud.spring.boot.common.redis.RedisResponseCallback} 回调函数
     */
    public void tryLock(String key, RedisResponseCallback callback) {
        tryLock(key, expiredTimeoutMillis, callback);
    }

    /**
     * Redis分布式锁 - 轻量级（尝试锁一次）
     * <p>
     * 在主从同步时，key未同步成功时，主挂，从为主时，会出现锁两次的情况
     *
     * @param key                  a {@link java.lang.String} key值
     * @param expiredTimeoutMillis Redis中Key超时时间，单位：毫秒
     * @param callback             a {@link link.thingscloud.spring.boot.common.redis.RedisResponseCallback} 回调函数
     */
    public void tryLock(String key, long expiredTimeoutMillis, RedisResponseCallback callback) {
        Boolean lock;
        try {
            lock = tryLock(lockKey + key, DateUtil.now(), expiredTimeoutMillis);
        } catch (Exception e) {
            callback.onException(e);
            return;
        }
        if (lock) {
            try {
                long start = System.currentTimeMillis();
                callback.onSucceed();
                long cost = System.currentTimeMillis() - start;
                if (cost >= expiredTimeoutMillis) {
                    // 存在风险
                    log.warn("[lock] key : {}, expired timeout : {} ms, cost : {} ms, you should increment timeout!", key, expiredTimeoutMillis, cost);
                } else {
                    log.debug("[lock] key : {}, expired timeout : {} ms, cost : {} ms", key, expiredTimeoutMillis, cost);
                }
            } catch (Exception e) {
                callback.onException(e);
            } finally {
                stringRedisTemplate.delete(lockKey + key);
            }
        } else {
            callback.onFailure();
        }
    }

    /**
     * Redis分布式锁 - 轻量级
     * <p>
     * 在主从同步时，key未同步成功时，主挂，从为主时，会出现锁两次的情况
     *
     * @param key               a {@link java.lang.String} key值
     * @param callback          a {@link link.thingscloud.spring.boot.common.redis.RedisResponseCallback} 回调函数
     */
    public void lock(String key, RedisResponseCallback callback) {
        lock(key, lockTimeoutMillis, expiredTimeoutMillis, callback);
    }

    /**
     * Redis分布式锁 - 轻量级
     * <p>
     * 在主从同步时，key未同步成功时，主挂，从为主时，会出现锁两次的情况
     *
     * @param key               a {@link java.lang.String} key值
     * @param lockTimeoutMillis 尝试锁的超时时间，单位：毫秒
     * @param callback          a {@link link.thingscloud.spring.boot.common.redis.RedisResponseCallback} 回调函数
     */
    public void lock(String key, long lockTimeoutMillis, RedisResponseCallback callback) {
        lock(key, lockTimeoutMillis, expiredTimeoutMillis, callback);
    }

    /**
     * Redis分布式锁 - 轻量级
     * <p>
     * 在主从同步时，key未同步成功时，主挂，从为主时，会出现锁两次的情况
     *
     * @param key                  a {@link java.lang.String} key值
     * @param lockTimeoutMillis    尝试锁的超时时间，单位：毫秒
     * @param expiredTimeoutMillis Redis中Key超时时间，单位：毫秒
     * @param callback             a {@link link.thingscloud.spring.boot.common.redis.RedisResponseCallback} 回调函数
     */
    public void lock(String key, long lockTimeoutMillis, long expiredTimeoutMillis, RedisResponseCallback callback) {
        Boolean lock;
        int tryTimes = 0;
        try {
            lock = tryLock(lockKey + key, DateUtil.now(), expiredTimeoutMillis);
            while (!lock && lockTimeoutMillis > 0L) {
                tryTimes++;
                lockTimeoutMillis -= sleepTimeoutMillis;
                ThreadUtil.sleep(sleepTimeoutMillis);
                lock = tryLock(lockKey + key, DateUtil.now(), expiredTimeoutMillis);
            }
        } catch (Exception e) {
            callback.onException(e);
            return;
        }
        if (lock) {
            try {
                long start = System.currentTimeMillis();
                callback.onSucceed();
                long cost = System.currentTimeMillis() - start;
                if (cost >= expiredTimeoutMillis) {
                    // 存在风险
                    log.warn("[lock] key : {}, lock timeout : {} ms, expired timeout : {} ms, tryTimes : {}, cost : {} ms, you should increment timeout!", key, lockTimeoutMillis, expiredTimeoutMillis, tryTimes, cost);
                } else {
                    log.debug("[lock] key : {}, lock timeout : {} ms, expired timeout : {} ms, tryTimes : {} cost : {} ms", key, lockTimeoutMillis, expiredTimeoutMillis, tryTimes, cost);
                }
            } catch (Exception e) {
                callback.onException(e);
            } finally {
                stringRedisTemplate.delete(lockKey + key);
            }
        } else {
            callback.onFailure();
        }
    }

    private Boolean tryLock(String key, String value, long timeoutMillis) {
        return stringRedisTemplate.opsForValue().setIfAbsent(key, value, timeoutMillis, TimeUnit.MILLISECONDS);
    }
}
