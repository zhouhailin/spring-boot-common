package link.thingscloud.spring.boot.common.example.data.redis;

import cn.hutool.core.date.StopWatch;
import cn.hutool.core.thread.ThreadUtil;
import link.thingscloud.spring.boot.common.example.CommonTest;
import link.thingscloud.spring.boot.common.redis.DistributedLock;
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
public class DistributedLockTest implements CommonTest {

    @Autowired
    private DistributedLock distributedLock;

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
        distributedLock.tryLock(key, callback);
        distributedLock.tryLock(key, callback);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        for (int i = 0; i < 10; i++) {
            distributedLock.tryLock(key, callback);
        }
        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());;
        for (int i = 0; i < 10; i++) {
            ThreadUtil.execute(() -> distributedLock.tryLock(key, callback));
        }
    }
    private void lock() {
        distributedLock.lock(key, 1000, callback);
        distributedLock.lock(key, 1000, callback);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        for (int i = 0; i < 10; i++) {
            distributedLock.lock(key, 1000, callback);
        }
        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());;
        for (int i = 0; i < 10; i++) {
            ThreadUtil.execute(() -> distributedLock.lock(key, 1000, callback));
        }
    }

    @PostConstruct
    @Override
    public void startup() {
        tryLock();

        lock();
    }

}
