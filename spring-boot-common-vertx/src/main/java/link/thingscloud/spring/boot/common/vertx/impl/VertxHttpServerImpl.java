package link.thingscloud.spring.boot.common.vertx.impl;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import link.thingscloud.spring.boot.common.vertx.VertxHttpServer;
import link.thingscloud.spring.boot.common.vertx.connection.ConnectionHandler;
import link.thingscloud.spring.boot.common.vertx.connection.ConnectionListener;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhouhailin
 * @since 1.4.0
 */
@Slf4j
public class VertxHttpServerImpl implements VertxHttpServer {

    private final int port;
    private final HttpServer httpServer;

    private final Map<String, ConnectionHandler> connectionHandlerMap = new ConcurrentHashMap<>(1024);
    private final Map<String, ConnectionListener> connectionListenerMap = new HashMap<>(32);

    public VertxHttpServerImpl(Vertx vertx, int port) {
        this.port = port;
        this.httpServer = vertx.createHttpServer(new HttpServerOptions().setMaxWebSocketFrameSize(1000000));
    }

    @Override
    public VertxHttpServerImpl start() {
        httpServer.webSocketHandler(webSocket -> {
            String path = webSocket.path();
            ConnectionListener listener = connectionListenerMap.get(path);
            if (listener == null) {
                webSocket.reject(404);
                return;
            }
            VertxConnectionHandlerImpl impl = new VertxConnectionHandlerImpl(webSocket.textHandlerID().replace("__vertx.ws.", ""), webSocket);
            impl.bind(listener, id -> connectionHandlerMap.put(id, impl), connectionHandlerMap::remove);
        });
        httpServer.listen(port, event -> log.info("http server start on port : {}", port));
        return this;
    }

    @Override
    public VertxHttpServerImpl shutdown() {
        httpServer.close(event -> log.info("http server shutdown."));
        return this;
    }

    @Override
    public ConnectionListener getConnectionListener(String uri) {
        return connectionListenerMap.get(uri);
    }

    @Override
    public VertxHttpServerImpl registerConnectionListener(String uri, ConnectionListener listener) {
        connectionListenerMap.put(uri, listener);
        return this;
    }

    @Override
    public ConnectionListener unregisterConnectionListener(String uri) {
        return connectionListenerMap.remove(uri);
    }

    @Override
    public ConnectionHandler getConnectionHandler(String id) {
        return connectionHandlerMap.get(id);
    }

}
