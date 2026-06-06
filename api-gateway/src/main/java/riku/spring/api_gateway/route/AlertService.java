package riku.spring.api_gateway.route;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.uri;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;

@Configuration
public class AlertService {
    @Bean
    public RouterFunction<ServerResponse> alertRoute(){
        return route("alert-service")
                .route(RequestPredicates.path("/api/alert/**"), http())
                .before(uri("http://localhost:8085"))
                .build();
    }
}
