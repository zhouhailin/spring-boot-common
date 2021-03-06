package link.thingscloud.spring.boot.common.util;


import org.junit.Test;

/**
 * @author zhouhailin
 * @since 1.1.0
 */
public class KeyUtilTest {

    @Test
    public void keys() {
        System.out.println(KeyUtil.keys("a", "b", "c"));
    }

    @Test
    public void testKeys() {
        System.out.println(KeyUtil.keys('#', "a", "b", "c"));
    }


}