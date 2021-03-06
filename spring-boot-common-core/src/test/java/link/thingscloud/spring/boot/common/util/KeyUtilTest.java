package link.thingscloud.spring.boot.common.util;


import org.junit.Assert;
import org.junit.Test;

/**
 * @author zhouhailin
 * @since 1.1.0
 */
public class KeyUtilTest {

    @Test
    public void keys() {
        Assert.assertEquals(KeyUtil.keys("a", "b", "c"), "a:b:c");
    }

    @Test
    public void testKeys() {
        Assert.assertEquals(KeyUtil.keys('#', "a", "b", "c"), "a#b#c");
    }


}