package riku.spring.Insight_Service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI insightServiceApiDocs(){
        return new OpenAPI()
                .info(
                        new Info()
                                .title("Insight Service API")
                                .description("Insight Service API for Home Energy Tracker")
                                .contact(getContact())
                                .license(getLicense())
                                .version("1.0.0")
                );
    }

    private io.swagger.v3.oas.models.info.Contact getContact(){
        return new Contact().name("Manabendu Karfa").email("technoriku@gmail.com");
    }

    private io.swagger.v3.oas.models.info.License getLicense(){
        return new License()
                .name("Creative Commons Attribution-NonCommercial 4.0 International License")
                .url("https://creativecommons.org/licenses/by-nc/4.0/");
    }
}



