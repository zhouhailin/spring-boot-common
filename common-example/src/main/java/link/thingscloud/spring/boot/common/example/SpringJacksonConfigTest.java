package link.thingscloud.spring.boot.common.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;

/**
 * @author zhouhailin
 * @since 1.2.0
 */
@Component
public class SpringJacksonConfigTest {

    @Autowired
    private ObjectMapper objectMapper;

    @PostConstruct
    public void start() throws JsonProcessingException {
        LocalDateTime now = LocalDateTime.now();
        System.out.println(objectMapper.writeValueAsString(new Person().setNow(now)));
        System.out.println(objectMapper.readValue("{\"now\":\"2021-03-24 01:07:05\"}", Person.class));
    }

    @Data
    @Accessors(chain = true)
    public static class Person {
        private LocalDateTime now;
    }
}
