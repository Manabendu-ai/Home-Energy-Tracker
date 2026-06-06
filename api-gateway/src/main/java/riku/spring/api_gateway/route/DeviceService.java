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
public class DeviceService {
    @Bean
    public RouterFunction<ServerResponse> deviceRoute(){
        return route("device-service")
                .route(RequestPredicates.path("/api/device/**"), http())
                .before(uri("http://localhost:8082"))
                .build();
    }
}
