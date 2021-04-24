package link.thingscloud.spring.boot.common.redis.impl;

import io.lettuce.core.internal.LettuceLists;
import link.thingscloud.spring.boot.common.redis.DistributedRedisLimiter;
import link.thingscloud.spring.boot.common.util.SystemClock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

/**
 * @author zhouhailin
 * @since 1.1.0
 */
@Slf4j
@Component
public class SimpleDistributedRedisLimiterImpl implements DistributedRedisLimiter {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static final String KEY_PREFIX = "simple:distributed:limiter:";

    private static final RedisScript<Long> SIMPLE_DISTRIBUTED_LIMITER_REDIS_SCRIPT = new DefaultRedisScript<>(
            "if redis.call('HLEN', KEYS[1]) < tonumber(KEYS[2]) then " +
                    " return redis.call('HSET', KEYS[1], ARGV[1], ARGV[2]) " +
                    "else" +
                    " return -1 " +
                    "end"
            , Long.class);

    @Override
    public boolean tryAcquire(String key, String hashKey, int permits) {
        try {
            Long result = stringRedisTemplate.execute(SIMPLE_DISTRIBUTED_LIMITER_REDIS_SCRIPT, LettuceLists.newList(KEY_PREFIX + key, String.valueOf(permits)), hashKey, SystemClock.strNow());
            return result != null && result > 0;
        } catch (Exception e) {
            log.error("try acquire key [{}] hashKey [{}] permits [{}] failed, cause : ", key, hashKey, permits, e);
        }
        return false;
    }

    @Override
    public boolean tryRelease(String key, String hashKey) {
        return stringRedisTemplate.opsForHash().delete(KEY_PREFIX + key, hashKey) == 1;
    }

}
