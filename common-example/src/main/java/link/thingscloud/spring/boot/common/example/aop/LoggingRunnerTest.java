package link.thingscloud.spring.boot.common.example.aop;

import cn.hutool.core.date.DateUtil;
import link.thingscloud.spring.boot.common.example.CommonTest;
import link.thingscloud.spring.boot.common.util.ExecutorHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * @author zhouhailin
 */
@Slf4j
@Component
public class LoggingRunnerTest implements CommonTest {

    @Autowired
    private LoggingTest loggingTest;

    @PostConstruct
    @Override
    public void startup() {
        ExecutorHelper.schedule(() -> {
            loggingTest.echo(DateUtil.now());
            loggingTest.echo(DateUtil.now());
            loggingTest.echo(DateUtil.now());
        }, 3, TimeUnit.SECONDS);
        ExecutorHelper.schedule(() -> {
            loggingTest.echo(DateUtil.now());
            loggingTest.echo(DateUtil.now());
            loggingTest.echo(DateUtil.now());
        }, 3, TimeUnit.SECONDS);
    }
}
