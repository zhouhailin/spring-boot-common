package link.thingscloud.spring.boot.common.example.aop;

import link.thingscloud.spring.boot.common.aop.Logging;
import link.thingscloud.spring.boot.common.aop.LoggingLevel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author zhouhailin
 */
@Slf4j
@Component
public class LoggingTest {

    @Logging(level = LoggingLevel.DEBUG, result = false)
    public String echo(String now) {
        return now;
    }

}
