package link.thingscloud.spring.boot.common.util;

/**
 * @author zhouhailin
 * @since 1.1.0
 */
public class KeyUtil {

    private static final char DEFAULT_CHAR = ':';

    public static String keys(String... keys) {
        return keys(DEFAULT_CHAR, keys);
    }

    public static String keys(char ch, String... keys) {
        StringBuilder sb = new StringBuilder();
        for (String key : keys) {
            sb.append(key).append(ch);
        }
        return sb.deleteCharAt(sb.length() - 1).toString();
    }

    private KeyUtil() {
    }

}
