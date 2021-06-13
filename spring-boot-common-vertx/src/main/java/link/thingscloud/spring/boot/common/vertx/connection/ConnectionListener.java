package link.thingscloud.spring.boot.common.vertx.connection;

/**
 * @author zhouhailin
 * @since 1.4.0
 */
public interface ConnectionListener {

    void onOpened(ConnectionHandler handler);

    void onMessage(ConnectionHandler handler, String message);

    void onClosed(ConnectionHandler handler);

    default void onException(ConnectionHandler handler, Throwable cause) {
    }
}
