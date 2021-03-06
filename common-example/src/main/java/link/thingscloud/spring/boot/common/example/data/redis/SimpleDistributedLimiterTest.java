package link.thingscloud.spring.boot.common.example.data.redis;

import cn.hutool.core.util.IdUtil;
import link.thingscloud.spring.boot.common.redis.SimpleDistributedLimiter;
import link.thingscloud.spring.boot.common.util.SystemClock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;

/**
 * @author zhouhailin
 * @since 1.1.0
 */
@Slf4j
@Component
public class SimpleDistributedLimiterTest {

    @Autowired
    private SimpleDistributedLimiter simpleDistributedLimiter;

    @PostConstruct
    public void startup() {
        for (int i = 0; i < 10; i++) {
            simpleDistributedLimiter.permit("test01", 6);
            simpleDistributedLimiter.permit("test02", 6);
            simpleDistributedLimiter.permit("test03", 5);
            simpleDistributedLimiter.permit("test04", 5);
        }
        // 21
        boolean permit = simpleDistributedLimiter.permit0("test01", IdUtil.fastUUID(), SystemClock.strNow(), 8, Arrays.asList("test02", "test03", "test04"), 25);
        System.out.println(permit);

//        simpleDistributedLimiter.clear(1000L);
    }
}
