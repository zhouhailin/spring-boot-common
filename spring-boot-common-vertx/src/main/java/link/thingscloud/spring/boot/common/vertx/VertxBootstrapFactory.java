package link.thingscloud.spring.boot.common.vertx;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import link.thingscloud.spring.boot.common.vertx.impl.VertxHttpClientImpl;
import link.thingscloud.spring.boot.common.vertx.impl.VertxHttpServerImpl;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhouhailin
 * @since 1.4.0
 */
@Slf4j
public class VertxBootstrapFactory {

    public static final int HTTP_SERVER_PORT = 9876;

    private static final Vertx VERTX = Vertx.vertx(new VertxOptions());

    private VertxBootstrapFactory() {
    }

    public static Vertx getVertx() {
        return VERTX;
    }

    public static VertxHttpServer createHttpServer() {
        return createHttpServer(HTTP_SERVER_PORT);
    }

    public static VertxHttpServer createHttpServer(int port) {
        return new VertxHttpServerImpl(VERTX, port);
    }

    public static VertxHttpClient createHttpClient() {
        return new VertxHttpClientImpl(VERTX);
    }

}
