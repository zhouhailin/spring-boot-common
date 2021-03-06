package link.thingscloud.spring.boot.common.redis;

import cn.hutool.core.util.IdUtil;
import io.lettuce.core.internal.LettuceLists;
import link.thingscloud.spring.boot.common.util.SystemClock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhouhailin
 * @since 1.1.0
 */
@Slf4j
@Component
public class SimpleDistributedLimiter {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static final String DEFAULT = "default";
    private static final String KEY_PREFIX = "simple:distributed:limiter:";

    private static final RedisScript<Long> SIMPLE_DISTRIBUTED_LIMITER_REDIS_SCRIPT = new DefaultRedisScript<>(
            "if redis.call('HLEN', KEYS[1]) < tonumber(KEYS[2]) then " +
                    " return redis.call('HSET', KEYS[1], ARGV[1], ARGV[2]) " +
                    "else" +
                    " return -1 " +
                    "end"
            , Long.class);

    public boolean permit(int permits) {
        return permit(DEFAULT, permits);
    }

    public boolean permit(String key, int permits) {
        return permit(key, IdUtil.fastSimpleUUID(), permits);
    }

    public boolean permit(String key, String hashKey, int permits) {
        try {
            Long result = stringRedisTemplate.execute(SIMPLE_DISTRIBUTED_LIMITER_REDIS_SCRIPT, LettuceLists.newList(KEY_PREFIX + key, String.valueOf(permits)), hashKey, SystemClock.strNow());
            return result != null && result > 0;
        } catch (Exception e) {
            log.error("permit key [{}] hashKey [{}] permits [{}] failed, cause : ", key, hashKey, permits, e);
        }
        return false;
    }

    public boolean permit0(String key, String hashKey, String hashValue, int permits, List<String> hashKeys, int maxPermits) {
        StringBuilder sb = new StringBuilder();
        sb.append("local value = redis.call('HLEN', KEYS[1]) ");
        sb.append("if value < tonumber(KEYS[2]) then ");
        if (hashKeys != null) {
            for (String hashKey0 : hashKeys) {
                sb.append(" value = value + redis.call('HLEN', '").append(hashKey0).append("') ");
            }
            sb.append(" if value < tonumber(KEYS[3]) then ");
            sb.append("  return redis.call('HSET', KEYS[1], ARGV[1], ARGV[2]) ");
            sb.append(" else ");
            sb.append("  return -1 ");
            sb.append(" end ");
        } else {
            sb.append(" return redis.call('HSET', KEYS[1], ARGV[1], ARGV[2]) ");
        }
        sb.append("else");
        sb.append(" return -2 ");
        sb.append("end");
        try {
            Long result = stringRedisTemplate.execute(new DefaultRedisScript<>(sb.toString(), Long.class), LettuceLists.newList(key, String.valueOf(permits), String.valueOf(maxPermits)), hashKey, hashValue);
            return result != null && result > 0;
        } catch (Exception e) {
            log.error("permit key [{}] hashKey [{}] permits [{}] failed, cause : ", key, hashKey, permits, e);
        }
        return false;
    }

    public List<String> clear(long expiredMillis) {
        return clear(DEFAULT, expiredMillis);
    }

    public List<String> clear(String key, long expiredMillis) {
        List<String> list = new ArrayList<>(8);
        stringRedisTemplate.<String, String>opsForHash().entries(KEY_PREFIX + key).forEach((key1, value) -> {
            if (SystemClock.now() - Long.parseLong(value) > expiredMillis) {
                list.add(key1);
            }
        });
        if (!list.isEmpty()) {
            stringRedisTemplate.opsForHash().delete(KEY_PREFIX + key, list.toArray());
        }
        return list;
    }

    public Boolean delete(String key) {
        return stringRedisTemplate.delete(KEY_PREFIX + key);
    }

}
