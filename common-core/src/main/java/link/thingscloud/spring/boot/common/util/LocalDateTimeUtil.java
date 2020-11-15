package link.thingscloud.spring.boot.common.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

/**
 * <p>LocalDateTimeUtil class.</p>
 *
 * @author zhouhailin
 * @version 1.0.0
 */
public class LocalDateTimeUtil {

    private static final ZoneId DEFAULT_ZONE_ID = ZoneId.systemDefault();

    private LocalDateTimeUtil() {
    }

    /**
     * <p>ofEpochMilli.</p>
     *
     * @param epochMilli a long.
     * @return a {@link java.time.LocalDateTime} object.
     */
    public static LocalDateTime ofEpochMilli(long epochMilli) {
        if (epochMilli <= 0L) {
            return null;
        }
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(epochMilli), DEFAULT_ZONE_ID);
    }

    /**
     * <p>ofUepochMilli.</p>
     *
     * @param uepochMilli a long.
     * @return a {@link java.time.LocalDateTime} object.
     */
    public static LocalDateTime ofUepochMilli(long uepochMilli) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(uepochMilli / 1000L), DEFAULT_ZONE_ID);
    }

    /**
     * <p>toEpochMilli.</p>
     *
     * @param uepochMillis a long.
     * @return a long.
     */
    public static long toEpochMilli(long uepochMillis) {
        return uepochMillis / 1000L;
    }

    /**
     * <p>epochToSeconds.</p>
     *
     * @param epochMillis a long.
     * @return a int.
     */
    public static int epochToSeconds(long epochMillis) {
        return (int) (epochMillis / 1000L);
    }

    /**
     * <p>uepochToSeconds.</p>
     *
     * @param uepochMillis a long.
     * @return a int.
     */
    public static int uepochToSeconds(long uepochMillis) {
        return (int) (uepochMillis / 1000_000L);
    }

    /**
     * <p>toEpochMilli.</p>
     *
     * @param localDateTime a {@link java.time.LocalDateTime} object.
     * @return a long.
     */
    public static long toEpochMilli(LocalDateTime localDateTime) {
        return localDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }
}
