package link.thingscloud.spring.boot.common.example.data.redis;

import cn.hutool.core.date.StopWatch;
import cn.hutool.core.thread.ThreadUtil;
import link.thingscloud.spring.boot.common.example.CommonTest;
import link.thingscloud.spring.boot.common.redis.SimpleDistributedLocker;
import link.thingscloud.spring.boot.common.redis.callback.RedisResponseCallback;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * @author zhouhailin
 */
@Slf4j
//@Component
public class DistributedLockerTest implements CommonTest {

    @Autowired
    private SimpleDistributedLocker simpleDistributedLocker;

    private final String key = getClass().getSimpleName();

    private final RedisResponseCallback callback = new RedisResponseCallback() {
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
        simpleDistributedLocker.tryLock(key, callback);
        simpleDistributedLocker.tryLock(key, callback);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        for (int i = 0; i < 10; i++) {
            simpleDistributedLocker.tryLock(key, callback);
        }
        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());
        for (int i = 0; i < 10; i++) {
            ThreadUtil.execute(() -> simpleDistributedLocker.tryLock(key, callback));
        }
    }

    private void lock() {
        simpleDistributedLocker.lock(key, 1000, callback);
        simpleDistributedLocker.lock(key, 1000, callback);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        for (int i = 0; i < 10; i++) {
            simpleDistributedLocker.lock(key, 1000, callback);
        }
        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());
        for (int i = 0; i < 10; i++) {
            ThreadUtil.execute(() -> simpleDistributedLocker.lock(key, 1000, callback));
        }
    }

    @PostConstruct
    @Override
    public void startup() {
        tryLock();

        lock();
    }

}
