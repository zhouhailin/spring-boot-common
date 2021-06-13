package link.thingscloud.spring.boot.common.vertx;

import link.thingscloud.spring.boot.common.vertx.connection.ConnectionListener;
import link.thingscloud.spring.boot.common.vertx.connection.ReconnectionHandler;
import link.thingscloud.spring.boot.common.vertx.impl.VertxHttpClientImpl;

import static link.thingscloud.spring.boot.common.vertx.VertxBootstrapFactory.HTTP_SERVER_PORT;

/**
 * @author zhouhailin
 * @since 1.4.0
 */
public interface VertxHttpClient {

    default VertxHttpClient connect(String path, ConnectionListener listener) {
        return connect("127.0.0.1", path, listener);
    }

    default VertxHttpClient connect(String host, String path, ConnectionListener listener) {
        return connect(host, HTTP_SERVER_PORT, path, listener);
    }

    VertxHttpClientImpl disconnect(String host, int port, String path);

    VertxHttpClient connect(String host, int port, String path, ConnectionListener listener);

    VertxHttpClientImpl connect(String host, int port, String path, ConnectionListener listener, ReconnectionHandler reconnectionHandler);

    VertxHttpClient start();

    VertxHttpClient shutdown();

}
