package link.thingscloud.spring.boot.common.example.data.redis;

import cn.hutool.core.date.StopWatch;
import cn.hutool.core.thread.ThreadUtil;
import link.thingscloud.spring.boot.common.example.CommonTest;
import link.thingscloud.spring.boot.common.redis.DistributedLocker;
import link.thingscloud.spring.boot.common.redis.RedisResponseCallback;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author zhouhailin
 */
@Slf4j
@Component
public class DistributedLockerTest implements CommonTest {

    @Autowired
    private DistributedLocker distributedLocker;

    private String key = getClass().getSimpleName();

    private RedisResponseCallback callback = new RedisResponseCallback() {
        @Override
        public void onSucceed() {
            log.info("lock {} onSucceed", key);
        }

        @Override
        public void onFailure() {
            log.info("lock {} onFailure", key);
        }

        @Override
        public void onException(Throwable cause) {
            log.error("lock {} onException", key, cause);
        }
    };

    private void tryLock() {
        distributedLocker.tryLock(key, callback);
        distributedLocker.tryLock(key, callback);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        for (int i = 0; i < 10; i++) {
            distributedLocker.tryLock(key, callback);
        }
        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());
        ;
        for (int i = 0; i < 10; i++) {
            ThreadUtil.execute(() -> distributedLocker.tryLock(key, callback));
        }
    }

    private void lock() {
        distributedLocker.lock(key, 1000, callback);
        distributedLocker.lock(key, 1000, callback);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        for (int i = 0; i < 10; i++) {
            distributedLocker.lock(key, 1000, callback);
        }
        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());
        ;
        for (int i = 0; i < 10; i++) {
            ThreadUtil.execute(() -> distributedLocker.lock(key, 1000, callback));
        }
    }

    @PostConstruct
    @Override
    public void startup() {
        tryLock();

        lock();
    }

}
