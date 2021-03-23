package link.thingscloud.spring.boot.common.redis;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.ThreadUtil;
import link.thingscloud.spring.boot.common.constant.NumberConstant;
import link.thingscloud.spring.boot.common.redis.callback.RedisResponseCallback;
import link.thingscloud.spring.boot.common.util.ExecutorHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author zhouhailin
 * @version 1.1.0
 */
@Slf4j
@Component
public class SimpleDistributedLocker {

    @Value("${simple.distributed.locker.sleepTimeout:10}")
    private long sleepTimeoutMillis;
    @Value("${simple.distributed.locker.lockTimeout:1000}")
    private long lockTimeoutMillis;
    @Value("${simple.distributed.locker.expiredTimeout:60000}")
    private long expiredTimeoutMillis;

    @Value("${simple.distributed.locker.key:simple:distributed:locker:}")
    private String lockerKey;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private final boolean isTraceEnabled = log.isTraceEnabled();

    private final Map<String, Long> expiredKeyMap = new ConcurrentHashMap<>(NumberConstant.NUM_16);

    private static final RedisScript<Long> DELETE_VALUE_REDIS_SCRIPT = new DefaultRedisScript<>(
            "if redis.call('GET', KEYS[1]) == ARGV[1] then " +
                    " return redis.call('DEL', KEYS[1]) " +
                    "else" +
                    " return -1 " +
                    "end"
            , Long.class);


    @PostConstruct
    public void startup() {
        // 28 60
        long intervalTimeMillis = (expiredTimeoutMillis / 2) - 2;
        ExecutorHelper.scheduleAtFixedRate(() -> {
            log.debug("scheduleAtFixedRate running at {}", DateUtil.now());
            long timeMillis = System.currentTimeMillis();
            expiredKeyMap.forEach((key, expiredTimeMillis) -> {
                if ((expiredTimeMillis - timeMillis) < intervalTimeMillis) {
                    try {
                        stringRedisTemplate.expire(key, expiredTimeoutMillis, TimeUnit.MILLISECONDS);
                        log.info("scheduleAtFixedRate expire key : [{}], expiredTimeMillis : {} ms", key, expiredTimeMillis);
                    } catch (Exception e) {
                        log.error("scheduleAtFixedRate expire key : [{}], expiredTimeMillis : {} ms", key, expiredTimeMillis, e);
                    }
                }
            });
        }, intervalTimeMillis, intervalTimeMillis, TimeUnit.MILLISECONDS);
    }

    /**
     * Redis分布式锁 - 轻量级（尝试锁一次）
     * <p>
     * 在主从同步时，key未同步成功时，主挂，从为主时，会出现锁两次的情况
     *
     * @param key      a {@link String} key值
     * @param callback a {@link RedisResponseCallback} 回调函数
     */
    public void tryLock(String key, RedisResponseCallback callback) {
        Boolean lock;
        String key0 = lockerKey + key;
        String value = DateUtil.now();
        long start = System.currentTimeMillis();
        try {
            lock = tryLock(key0, value, expiredTimeoutMillis);
        } catch (Exception e) {
            callback.onException(e);
            return;
        }
        if (lock != null && lock) {
            try {
                expiredKeyMap.put(key0, start + expiredTimeoutMillis);
                callback.onSucceed();
                long cost = System.currentTimeMillis() - start;
                log.debug("tryLock key : {}, expired timeout : {} ms, cost : {} ms", key, expiredTimeoutMillis, cost);
            } catch (Exception e) {
                callback.onException(e);
            } finally {
                expiredKeyMap.remove(key0);
                deleteLock(key0, value);
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
     * @param key      a {@link String} key值
     * @param callback a {@link RedisResponseCallback} 回调函数
     */
    public void lock(String key, RedisResponseCallback callback) {
        lock(key, lockTimeoutMillis, callback);
    }

    /**
     * Redis分布式锁 - 轻量级
     * <p>
     * 在主从同步时，key未同步成功时，主挂，从为主时，会出现锁两次的情况
     *
     * @param key               a {@link String} key值
     * @param lockTimeoutMillis 尝试锁的超时时间，单位：毫秒
     * @param callback          a {@link RedisResponseCallback} 回调函数
     */
    public void lock(String key, long lockTimeoutMillis, RedisResponseCallback callback) {
        Boolean lock;
        int tryTimes = 0;
        String key0 = lockerKey + key;
        String value = DateUtil.now();
        try {
            lock = tryLock(key0, value, expiredTimeoutMillis);
            while (!lock && lockTimeoutMillis > 0L) {
                tryTimes++;
                lockTimeoutMillis -= sleepTimeoutMillis;
                ThreadUtil.sleep(sleepTimeoutMillis);
                lock = tryLock(key0, value, expiredTimeoutMillis);
            }
        } catch (Exception e) {
            callback.onException(e);
            return;
        }
        if (lock) {
            try {
                long start = System.currentTimeMillis();
                expiredKeyMap.put(key0, start + expiredTimeoutMillis);
                callback.onSucceed();
                long cost = System.currentTimeMillis() - start;
                log.debug("lock key : {}, lock timeout : {} ms, expired timeout : {} ms, tryTimes : {}, cost : {} ms", key, lockTimeoutMillis, expiredTimeoutMillis, tryTimes, cost);
            } catch (Exception e) {
                callback.onException(e);
            } finally {
                expiredKeyMap.remove(key0);
                deleteLock(key0, value);
            }
        } else {
            callback.onFailure();
        }
    }

    private Boolean tryLock(String key, String value, long timeoutMillis) {
        return stringRedisTemplate.opsForValue().setIfAbsent(key, value, timeoutMillis, TimeUnit.MILLISECONDS);
    }

    private void deleteLock(String key, String value) {
        stringRedisTemplate.execute(DELETE_VALUE_REDIS_SCRIPT, Collections.singletonList(key), value);
    }
}
