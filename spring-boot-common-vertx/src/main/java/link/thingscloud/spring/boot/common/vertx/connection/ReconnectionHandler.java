package link.thingscloud.spring.boot.common.vertx.connection;

/**
 * @author zhouhailin
 * @since 1.4.0
 */
public interface ReconnectionHandler {

    String id();

    void id(String id);

    default void reconnection(boolean reconnection) {
    }

    boolean reconnection();

    class DefaultReconnectionHandler implements ReconnectionHandler {
        private String id;
        private boolean reconnection = true;

        @Override
        public String id() {
            return id;
        }

        @Override
        public void id(String id) {
            this.id = id;
        }

        @Override
        public void reconnection(boolean reconnection) {
            this.reconnection = reconnection;
        }

        @Override
        public boolean reconnection() {
            return this.reconnection;
        }
    }

    ReconnectionHandler DEFAULT_RECONNECTION_HANDLER = new ReconnectionHandler() {
        private String id;

        public String id() {
            return id;
        }

        public void id(String id) {
            this.id = id;
        }

        @Override
        public boolean reconnection() {
            return true;
        }
    };
}
