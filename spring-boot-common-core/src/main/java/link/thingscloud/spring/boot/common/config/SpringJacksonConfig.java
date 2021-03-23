package link.thingscloud.spring.boot.common.config;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import link.thingscloud.spring.boot.common.properties.CommonProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.Formatter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * @author zhouhailin
 * @since 1.2.0
 */
@Configuration
public class SpringJacksonConfig {

    @Autowired
    private CommonProperties commonProperties;

    @Bean
    public LocalDateTimeSerializer localDateTimeSerializer() {
        return new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(commonProperties.getDateTimeFormat()));
    }

    @Bean
    public LocalDateTimeDeserializer localDateTimeDeserializer() {
        return new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(commonProperties.getDateTimeFormat()));
    }

    @Bean
    public LocalDateSerializer localDateSerializer() {
        return new LocalDateSerializer(DateTimeFormatter.ofPattern(commonProperties.getDateFormat()));
    }

    @Bean
    public LocalDateDeserializer localDateDeserializer() {
        return new LocalDateDeserializer(DateTimeFormatter.ofPattern(commonProperties.getDateFormat()));
    }

    @Bean
    public LocalTimeSerializer localTimeSerializer() {
        return new LocalTimeSerializer(DateTimeFormatter.ofPattern(commonProperties.getTimeFormat()));
    }

    @Bean
    public LocalTimeDeserializer localTimeDeserializer() {
        return new LocalTimeDeserializer(DateTimeFormatter.ofPattern(commonProperties.getTimeFormat()));
    }

    @Bean
    public Formatter<LocalDateTime> localDateTimeFormatter() {
        return new Formatter<LocalDateTime>() {
            @Override
            public LocalDateTime parse(String text, Locale locale) {
                return LocalDateTime.parse(text, DateTimeFormatter.ofPattern(commonProperties.getDateTimeFormat()));
            }

            @Override
            public String print(LocalDateTime localDateTime, Locale locale) {
                return DateTimeFormatter.ofPattern(commonProperties.getDateTimeFormat()).format(localDateTime);
            }
        };
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> builder
                //时间类型的序列化与反序列化 用于返回时间格式与接收时间格式
                .serializerByType(LocalDateTime.class, localDateTimeSerializer())
                .deserializerByType(LocalDateTime.class, localDateTimeDeserializer())
                .serializerByType(LocalDate.class, localDateSerializer())
                .deserializerByType(LocalDate.class, localDateDeserializer())
                .serializerByType(LocalTime.class, localTimeSerializer())
                .deserializerByType(LocalTime.class, localTimeDeserializer());
    }

}
