package link.thingscloud.spring.boot.common.example.core.util;

import com.alibaba.fastjson.JSON;

import java.util.List;

/**
 * @author zhouhailin
 * @since 1.1.0
 */
public class JsonUtilTest {
    public static void main(String[] args) {
        JSON.toJSONString(new Object());

        JSON.parseObject("", JsonUtilTest.class);


        List<JsonUtilTest> jsonUtilTests = JSON.parseArray("", JsonUtilTest.class);
    }
}
