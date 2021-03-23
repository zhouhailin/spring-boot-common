package link.thingscloud.spring.boot.common.redis;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author zhouhailin
 * @since 1.1.0
 */
class SimpleDistributedLimiterTest {

    @Autowired
    private SimpleDistributedLimiter simpleDistributedLimiter;

    @Test
    void permit() {
        simpleDistributedLimiter.clear(0);
        assertTrue(simpleDistributedLimiter.permit(5));
        assertTrue(simpleDistributedLimiter.permit(5));
        assertTrue(simpleDistributedLimiter.permit(5));
        assertTrue(simpleDistributedLimiter.permit(5));
        assertTrue(simpleDistributedLimiter.permit(5));

        assertFalse(simpleDistributedLimiter.permit(5));
        assertFalse(simpleDistributedLimiter.permit(5));

    }

    @Test
    void testPermit() {
    }

    @Test
    void testPermit1() {
    }

    @Test
    void testPermit2() {
    }

    @Test
    void testPermit3() {
    }

    @Test
    void testPermit4() {
    }

    @Test
    void clear() {
    }

    @Test
    void delete() {
    }
}