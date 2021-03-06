package link.thingscloud.spring.boot.common.redis.annotation;

import java.lang.annotation.*;

/**
 * @author zhouhailin
 * @since 1.1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RedisTopic {

    String value();

}
