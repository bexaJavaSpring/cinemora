package bekhruz.com.cinemora.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
@EnableConfigurationProperties(FileResourceProperties.class)
public class FileResourceConfig implements WebMvcConfigurer {
    private final FileResourceProperties properties;

    public FileResourceConfig(FileResourceProperties fileResourceProperties) {
        this.properties = fileResourceProperties;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/lxp-files/**")
                .addResourceLocations(properties.absolutePath())
                .setCachePeriod(3600);
    }
}
