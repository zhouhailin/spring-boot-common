package link.thingscloud.spring.boot.common.util;

import cn.hutool.crypto.digest.DigestUtil;
import org.springframework.http.HttpHeaders;

/**
 * @author zhouhailin
 * @since 1.0.0
 */
public class AccessUtil {

    public static final String ACCESS_KEY_ID = "access-key-id";
    public static final String ACCESS_KEY_SECRET = "access-key-secret";
    public static final String ACCESS_TIMESTAMP = "access-timestamp";
    public static final String ACCESS_TOKEN = "access-token";

    private AccessUtil() {
    }

    public static String token(String keyId, String keySecret, String timestamp) {
        return DigestUtil.md5Hex(keyId + timestamp + keySecret);
    }

    public static String token(String keyId, String keySecret, long timestamp) {
        return DigestUtil.md5Hex(keyId + timestamp + keySecret);
    }

    public static HttpHeaders newHttpHeader(String keyId, String keySecret) {
        return newHttpHeader(keyId, keySecret, SystemClock.strNow());
    }

    public static HttpHeaders newHttpHeader(String keyId, String keySecret, long timestamp) {
        return newHttpHeader(keyId, keySecret, String.valueOf(timestamp));
    }

    public static HttpHeaders newHttpHeader(String keyId, String keySecret, String timestamp) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(ACCESS_KEY_ID, keyId);
        httpHeaders.add(ACCESS_TIMESTAMP, timestamp);
        httpHeaders.add(ACCESS_TOKEN, DigestUtil.md5Hex(keyId + timestamp + keySecret));
        return httpHeaders;
    }

}
