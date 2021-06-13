package link.thingscloud.spring.boot.common.example.vertx;

import link.thingscloud.spring.boot.common.vertx.VertxBootstrapFactory;

/**
 * @author zhouhailin
 * @since 1.0.0
 */
public class VertxExample {

    public static void main(String[] args) {
        VertxBootstrapFactory.getVertx();
    }

}
