package link.thingscloud.spring.boot.common.util;

import cn.hutool.crypto.digest.DigestUtil;

/**
 * @author zhouhailin
 * @since 1.0.0
 */
public class AccessUtil {

    private AccessUtil() {
    }

    public static String token(String keyId, String keySecret, String timestamp) {
        return DigestUtil.md5Hex(keyId + timestamp + keySecret);
    }

    public static String token(String keyId, String keySecret, long timestamp) {
        return DigestUtil.md5Hex(keyId + timestamp + keySecret);
    }

}
