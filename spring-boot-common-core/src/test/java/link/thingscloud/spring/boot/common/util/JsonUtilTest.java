package link.thingscloud.spring.boot.common.util;

import lombok.Data;
import lombok.experimental.Accessors;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

/**
 * @author zhouhailin
 * @since 1.1.0
 */
public class JsonUtilTest {

    Person person = new Person().setName("test").setAge(10)
            .setLocalDate(LocalDate.now())
            .setLocalTime(LocalTime.now())
            .setLocalDateTime(LocalDateTime.now())
            .setDate(new Date(System.currentTimeMillis()));

    String json1 = "{\"name\":\"test\",\"age\":10,\"localDateTime\":\"2021-03-03 12:08:18.139\",\"date\":1614744498139}";
    String json2 = "[{\"name\":\"mkyong\", \"age\":37}, {\"name\":\"fong\", \"age\":38}]";

    @Test
    public void toJSONString() {
        System.out.println(JsonUtil.toJSONString(person));
    }

    @Test
    public void writeValue() {
        System.out.println(JsonUtil.writeValue(person));
    }

    @Test
    public void parseObject() {
        System.out.println(JsonUtil.parseObject("{\"name\":\"test\",\"age\":10,\"localDateTime\":\"2021-03-03 12:29:34.849\",\"localDate\":\"2021-03-03\",\"localTime\":\"12:29:34.849\",\"date\":1614745774850}", Person.class));
    }

    @Test
    public void readValue() {
        System.out.println(JsonUtil.readValue(json1, Person.class));
    }

    @Test
    public void parseArray() {
        System.out.println(JsonUtil.parseArray(json2));
        System.out.println(JsonUtil.parseArray("[]"));
    }


    @Data
    @Accessors(chain = true)
    static class Person {
        private String name;
        private Integer age;
        private LocalDateTime localDateTime;
        private LocalDate localDate;
        private LocalTime localTime;
        private Date date;
    }
}