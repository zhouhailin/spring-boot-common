package link.thingscloud.spring.boot.common.vertx.connection;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.net.SocketAddress;
import link.thingscloud.spring.boot.common.vertx.impl.VertxConnectionHandlerImpl;

/**
 * @author zhouhailin
 * @since 1.4.0
 */
public interface ConnectionHandler {

    String id();

    SocketAddress localAddress();

    SocketAddress remoteAddress();

    ConnectionHandler close();

    VertxConnectionHandlerImpl close(Handler<AsyncResult<Void>> handler);

    ConnectionHandler write(String text);

    ConnectionHandler write(String text, Handler<AsyncResult<Void>> handler);

}
