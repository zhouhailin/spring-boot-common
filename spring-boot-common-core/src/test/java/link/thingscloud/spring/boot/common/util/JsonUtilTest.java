package link.thingscloud.spring.boot.common.util;

import lombok.Data;
import lombok.experimental.Accessors;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author zhouhailin
 * @since 1.1.0
 */
public class JsonUtilTest {

    Person person = new Person().setName("test").setAge(10)
            .setLocalDate(LocalDate.now().withYear(2021).withMonth(1).withDayOfMonth(1))
            .setLocalTime(LocalTime.now().withHour(1).withMinute(1).withSecond(1).withNano(1))
            .setLocalDateTime(LocalDateTime.now().withYear(2021).withMonth(1).withDayOfMonth(1).withHour(1).withMinute(1).withSecond(1).withNano(1))
            .setDate(new Date(1614744498139L));

    @Test
    public void writeValue() {
        assertEquals(JsonUtil.writeValue(person), "{\"name\":\"test\",\"age\":10,\"localDateTime\":\"2021-01-01 01:01:01.000\",\"localDate\":\"2021-01-01\",\"localTime\":\"01:01:01.000\",\"date\":1614744498139}");
    }

    @Test
    public void parseObject() {
        Person person = JsonUtil.parseObject("{\"name\":\"test\",\"age\":10,\"localDateTime\":\"2021-03-03 12:29:34.849\",\"localDate\":\"2021-03-03\",\"localTime\":\"12:29:34.849\",\"date\":1614745774850}", Person.class);
        assertNotNull(person);
        assertEquals(person.getName(), "test");
        assertEquals((long) person.getAge(), 10L);
        assertEquals(person.getLocalDateTime().getHour(), 12L);
    }

    @Test
    public void readValue() {
        String json1 = "{\"name\":\"test\",\"age\":10,\"localDateTime\":\"2021-03-03 12:08:18.139\",\"date\":1614744498139}";
        Person person = JsonUtil.readValue(json1, Person.class);
        assertNotNull(person);
        assertEquals(person.getName(), "test");
        assertEquals((long) person.getAge(), 10L);
        assertEquals(person.getLocalDateTime().getHour(), 12L);
    }

    @Test
    public void parseArray() {
        String json2 = "[{\"name\":\"mkyong\", \"age\":37}, {\"name\":\"fong\", \"age\":38}]";
        assertEquals(JsonUtil.parseArray(json2).size(), 2);
        Assert.assertTrue(JsonUtil.parseArray("[]").isEmpty());
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