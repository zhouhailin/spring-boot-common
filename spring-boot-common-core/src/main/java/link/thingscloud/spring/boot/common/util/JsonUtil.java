package link.thingscloud.spring.boot.common.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

/**
 * @author zhouhailin
 * @since 1.1.0
 */
@Slf4j
public class JsonUtil {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    private static final DateTimeFormatter DATE_TIME_FORMATTER_2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATE_TIME_FORMATTER_3 = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    static {
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        registerLocalDate(javaTimeModule);
        registerLocalTime(javaTimeModule);
        registerLocalDateTime(javaTimeModule);
        MAPPER.registerModule(javaTimeModule);
    }

    /**
     * 对象转JSON字符串
     *
     * @param object 对象
     * @return JSON字符串
     */
    public static String toString(Object object) {
        return writeValue(object);
    }

    /**
     * 对象转JSON字符串
     *
     * @param object 对象
     * @return JSON字符串
     */
    public static String writeValue(Object object) {
        try {
            return MAPPER.writeValueAsString(object);
        } catch (IOException e) {
            log.error("writeValue object [{}] failed, cause : ", object, e);
        }
        return null;
    }

    /**
     * JSON字符串转对象
     *
     * @param text  JSON字符串
     * @param clazz 对象类型
     * @param <T>   对象类型
     * @return 对象
     */
    public static <T> T parseObject(String text, Class<T> clazz) {
        return readValue(text, clazz);
    }

    /**
     * JSON字符串转对象
     *
     * @param text  JSON字符串
     * @param clazz 对象类型
     * @param <T>   对象类型
     * @return 对象
     */
    public static <T> T readValue(String text, Class<T> clazz) {
        try {
            return MAPPER.readValue(text, clazz);
        } catch (IOException e) {
            log.error("readValue text [{}], clazz name [{}] failed, cause : ", text, clazz.getName(), e);
        }
        return null;
    }

    public static <T> List<T> parseArray(String text) {
        try {
            return MAPPER.readValue(text, new TypeReference<List<T>>() {
            });
        } catch (IOException e) {
            log.error("parseArray text [{}] failed, cause : ", text, e);
        }
        return Collections.emptyList();
    }

    private static void registerLocalDateTime(JavaTimeModule javaTimeModule) {
        javaTimeModule.addSerializer(LocalDateTime.class, new JsonSerializer<LocalDateTime>() {
            @Override
            public void serialize(LocalDateTime value, JsonGenerator generator, SerializerProvider serializers) throws IOException {
                generator.writeString(DATE_TIME_FORMATTER.format(value));
            }
        });
        javaTimeModule.addDeserializer(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
            @Override
            public LocalDateTime deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException {
                return LocalDateTime.parse(parser.getText(), DATE_TIME_FORMATTER);
            }
        });
    }

    private static void registerLocalDate(JavaTimeModule javaTimeModule) {
        javaTimeModule.addSerializer(LocalDate.class, new JsonSerializer<LocalDate>() {
            @Override
            public void serialize(LocalDate value, JsonGenerator generator, SerializerProvider serializers) throws IOException {
                generator.writeString(DATE_TIME_FORMATTER_2.format(value));
            }
        });
        javaTimeModule.addDeserializer(LocalDate.class, new JsonDeserializer<LocalDate>() {
            @Override
            public LocalDate deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException {
                return LocalDate.parse(parser.getText(), DATE_TIME_FORMATTER_2);
            }
        });
    }

    private static void registerLocalTime(JavaTimeModule javaTimeModule) {
        javaTimeModule.addSerializer(LocalTime.class, new JsonSerializer<LocalTime>() {
            @Override
            public void serialize(LocalTime value, JsonGenerator generator, SerializerProvider serializers) throws IOException {
                generator.writeString(DATE_TIME_FORMATTER_3.format(value));
            }
        });
        javaTimeModule.addDeserializer(LocalTime.class, new JsonDeserializer<LocalTime>() {
            @Override
            public LocalTime deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException {
                return LocalTime.parse(parser.getText(), DATE_TIME_FORMATTER_3);
            }
        });
    }

    private JsonUtil() {
    }
}
