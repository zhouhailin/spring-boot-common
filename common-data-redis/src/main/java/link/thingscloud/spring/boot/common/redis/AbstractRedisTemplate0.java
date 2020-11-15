package link.thingscloud.spring.boot.common.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * <p>Abstract AbstractRedisTemplate0 class.</p>
 *
 * @author zhouhailin
 * @version 1.0.0
 */
@Slf4j
public abstract class AbstractRedisTemplate0 {
    @Autowired
    protected StringRedisTemplate stringRedisTemplate;
}
