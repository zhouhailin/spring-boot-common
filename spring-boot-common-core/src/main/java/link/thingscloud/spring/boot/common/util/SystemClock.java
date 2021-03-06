package link.thingscloud.spring.boot.common.util;

import java.time.Instant;

/**
 * @author zhouhailin
 * @since 1.1.0
 */
public class SystemClock {

    public static long now() {
        return Instant.now().toEpochMilli();
    }

    public static String strNow() {
        return String.valueOf(Instant.now().toEpochMilli());
    }

    private SystemClock() {
    }
}
