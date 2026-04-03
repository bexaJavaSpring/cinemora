package bekhruz.com.cinemora.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "file")
public record FileResourceProperties(String absolutePath) {
}
