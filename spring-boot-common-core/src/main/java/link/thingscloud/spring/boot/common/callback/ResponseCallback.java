package link.thingscloud.spring.boot.common.callback;

/**
 * @author zhouhailin
 * @since 1.1.0
 */
public interface ResponseCallback {

    /**
     * <p>onSucceed.</p>
     */
    void onSucceed();

    /**
     * <p>onFailure.</p>
     */
    default void onFailure() {
    }

    /**
     * <p>onException.</p>
     *
     * @param cause a {@link java.lang.Throwable} object.
     */
    default void onException(Throwable cause) {
        throw new RuntimeException(cause);
    }

}
