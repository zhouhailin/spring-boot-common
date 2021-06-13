package link.thingscloud.spring.boot.common.vertx.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.http.WebSocketBase;
import io.vertx.core.net.SocketAddress;
import link.thingscloud.spring.boot.common.vertx.connection.ConnectionHandler;
import link.thingscloud.spring.boot.common.vertx.connection.ConnectionListener;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author zhouhailin
 * @since 1.4.0
 */
public class VertxConnectionHandlerImpl implements ConnectionHandler {

    private final String id;
    private final WebSocketBase webSocket;

    protected VertxConnectionHandlerImpl(String id, WebSocketBase webSocket) {
        this.id = id;
        this.webSocket = webSocket;
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public SocketAddress localAddress() {
        return webSocket.localAddress();
    }

    @Override
    public SocketAddress remoteAddress() {
        return webSocket.remoteAddress();
    }

    @Override
    public VertxConnectionHandlerImpl close() {
        webSocket.close();
        return this;
    }

    @Override
    public VertxConnectionHandlerImpl close(Handler<AsyncResult<Void>> handler) {
        webSocket.close(handler);
        return this;
    }

    @Override
    public VertxConnectionHandlerImpl write(String text) {
        webSocket.writeTextMessage(text);
        return this;
    }

    @Override
    public VertxConnectionHandlerImpl write(String text, Handler<AsyncResult<Void>> handler) {
        webSocket.writeTextMessage(text, handler);
        return this;
    }

    public void bind(ConnectionListener listener, Consumer<String> onOpened, Consumer<String> onClosed) {
        webSocket
                .frameHandler(event -> listener.onMessage(this, event.textData()))
                .exceptionHandler(event -> listener.onException(this, event))
                .closeHandler(event -> {
                    listener.onClosed(this);
                    onClosed.accept(id);
                });
        onOpened.accept(id);
        listener.onOpened(this);
    }

}
