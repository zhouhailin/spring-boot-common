package link.thingscloud.spring.boot.common.vertx;

import link.thingscloud.spring.boot.common.vertx.connection.ConnectionHandler;
import link.thingscloud.spring.boot.common.vertx.connection.ConnectionListener;
import link.thingscloud.spring.boot.common.vertx.impl.VertxHttpServerImpl;

/**
 * @author zhouhailin
 * @since 1.4.0
 */
public interface VertxHttpServer {

    VertxHttpServer start();

    VertxHttpServer shutdown();

    ConnectionListener getConnectionListener(String uri);

    VertxHttpServerImpl registerConnectionListener(String uri, ConnectionListener listener);

    ConnectionListener unregisterConnectionListener(String uri);

    ConnectionHandler getConnectionHandler(String id);
}
