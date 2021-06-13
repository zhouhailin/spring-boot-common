package link.thingscloud.spring.boot.common.vertx.impl;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.WebSocket;
import link.thingscloud.spring.boot.common.util.KeyUtil;
import link.thingscloud.spring.boot.common.vertx.VertxHttpClient;
import link.thingscloud.spring.boot.common.vertx.connection.ConnectionHandler;
import link.thingscloud.spring.boot.common.vertx.connection.ConnectionListener;
import link.thingscloud.spring.boot.common.vertx.connection.ReconnectionHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static link.thingscloud.spring.boot.common.vertx.connection.ReconnectionHandler.DEFAULT_RECONNECTION_HANDLER;

/**
 * @author zhouhailin
 * @since 1.4.0
 */
@Slf4j
public class VertxHttpClientImpl implements VertxHttpClient {

    private boolean started;
    private final Vertx vertx;
    private final HttpClient httpClient;

    private final Map<String, ConnectionHandler> connectionHandlerMap = new ConcurrentHashMap<>(1024);
    private final Map<String, ReconnectionHandler> reconnectionHandlerMap = new ConcurrentHashMap<>(32);


    public VertxHttpClientImpl(Vertx vertx) {
        this.vertx = vertx;
        this.httpClient = vertx.createHttpClient(new HttpClientOptions().setMaxWebSocketFrameSize(1000000));
    }

    @Override
    public VertxHttpClientImpl disconnect(String host, int port, String path) {
        ReconnectionHandler reconnectionHandler = reconnectionHandlerMap.remove(KeyUtil.keys(host, String.valueOf(port), path));
        if (reconnectionHandler == null) {
            return this;
        }
        reconnectionHandler.reconnection(false);
        if (reconnectionHandler.id() == null) {
            return this;
        }
        ConnectionHandler connectionHandler = connectionHandlerMap.get(reconnectionHandler.id());
        if (connectionHandler != null) {
            connectionHandler.close();
        }
        return this;
    }

    @Override
    public VertxHttpClientImpl connect(String host, int port, String path, ConnectionListener listener) {
        return connect(host, port, path, listener, DEFAULT_RECONNECTION_HANDLER);
    }

    @Override
    public VertxHttpClientImpl connect(String host, int port, String path, ConnectionListener listener, ReconnectionHandler reconnectionHandler) {
        if (!reconnectionHandler.reconnection()) {
            return this;
        }
        log.info("connect host : {}, port : {}, path : {}", host, port, path);
        String newValue = KeyUtil.keys(host, String.valueOf(port), path);
        reconnectionHandlerMap.put(newValue, reconnectionHandler);
        httpClient.webSocket(port, host, path, res -> {
            if (res.succeeded()) {
                WebSocket webSocket = res.result();
                VertxConnectionHandlerImpl impl = new VertxConnectionHandlerImpl(webSocket.textHandlerID().replace("__vertx.ws.", ""), webSocket);
                impl.bind(listener, id -> {
                    connectionHandlerMap.put(id, impl);
                    reconnectionHandler.id(id);
                }, id -> {
                    connectionHandlerMap.remove(id);
                    reconnectionHandler.id(null);
                    if (started) {
                        boolean reconnection = reconnectionHandlerMap.getOrDefault(newValue, DEFAULT_RECONNECTION_HANDLER).reconnection();
                        if (reconnection) {
                            vertx.setTimer(1000, event -> connect(host, port, path, listener, reconnectionHandler));
                        }
                    }
                });
                log.info("[{}] connect host : {}, port : {}, path : {} successful.", impl.id(), host, port, path);
            } else {
                reconnectionHandler.id(null);
                log.error("connect host : {}, port : {}, path : {} failed.", host, port, path, res.cause());
                if (started) {
                    boolean reconnection = reconnectionHandlerMap.getOrDefault(newValue, DEFAULT_RECONNECTION_HANDLER).reconnection();
                    if (reconnection) {
                        vertx.setTimer(1000, event -> connect(host, port, path, listener, reconnectionHandler));
                    }
                }
            }
        });
        return this;
    }

    @Override
    public VertxHttpClientImpl start() {
        this.started = true;
        log.info("http client start.");
        return this;
    }

    @Override
    public VertxHttpClientImpl shutdown() {
        this.started = false;
        connectionHandlerMap.forEach((id, connectionHandler) -> connectionHandler.close(event -> log.info("[{}] close handler completed", id)));
        httpClient.close(event -> log.info("http client shutdown."));
        return this;
    }

}
