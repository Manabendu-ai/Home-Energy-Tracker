package riku.spring.usage_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI usageServiceApiDocs(){
        return new OpenAPI()
                .info(
                        new Info()
                                .title("Usage Service API")
                                .description("Usage Service API for Home Energy Tracker")
                                .contact(getContact())
                                .license(getLicense())
                                .version("1.0.0")
                );
    }

    private Contact getContact(){
        return new Contact().name("Manabendu Karfa").email("technoriku@gmail.com");
    }

    private License getLicense(){
        return new License()
                .name("Creative Commons Attribution-NonCommercial 4.0 International License")
                .url("https://creativecommons.org/licenses/by-nc/4.0/");
    }
}

