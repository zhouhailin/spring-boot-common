package link.thingscloud.spring.boot.common.properties;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhouhailin
 * @since 1.2.0
 */
@Data
@Accessors(chain = true)
@Configuration
@ConfigurationProperties(prefix = "spring.common")
public class CommonProperties {
    /**
     * yyyy-MM-dd HH:mm:ss
     */
    private String dateTimeFormat = "yyyy-MM-dd HH:mm:ss";
    /**
     * yyyy-MM-dd
     */
    private String dateFormat = "yyyy-MM-dd";
    /**
     * HH:mm:ss
     */
    private String timeFormat = "HH:mm:ss";
}
