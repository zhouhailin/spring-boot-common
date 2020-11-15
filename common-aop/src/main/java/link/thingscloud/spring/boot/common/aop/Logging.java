package link.thingscloud.spring.boot.common.aop;

import java.lang.annotation.*;

/**
 * <p>Logging class.</p>
 *
 * @author : zhouhailin
 * @version 1.0.0
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Logging {

    LoggingLevel level() default LoggingLevel.INFO;

    boolean result() default true;

}
