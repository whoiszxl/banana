package com.whoiszxl.swagger;

import com.whoiszxl.core.properties.BananaProperties;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author whoiszxl
 */
@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(name = "banana.docs.enabled", havingValue = "true", matchIfMissing = true)
public class SwaggerConfiguration {

    private final BananaProperties bananaProperties;

    @Bean
    public OpenAPI openApi() {
        return new OpenAPI().info(new Info()
                .title(bananaProperties.getProjectName() + "接口文档")
                .version(bananaProperties.getVersion())
                .description(bananaProperties.getDescription())
                .contact(bananaProperties.getContact())
                .license(bananaProperties.getLicense())
        );
    }

}
