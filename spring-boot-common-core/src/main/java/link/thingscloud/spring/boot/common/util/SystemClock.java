package link.thingscloud.spring.boot.common.util;

/**
 * @author zhouhailin
 * @since 1.1.0
 */
public class SystemClock {

    public static long now() {
        return System.currentTimeMillis();
    }

    public static String strNow() {
        return String.valueOf(System.currentTimeMillis());
    }

    private SystemClock() {
    }
}
