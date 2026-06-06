package riku.spring.api_gateway.route;

import org.springframework.cloud.gateway.server.mvc.filter.CircuitBreakerFilterFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import java.net.URI;

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
                .filter(CircuitBreakerFilterFunctions.circuitBreaker(
                        "AlertServiceCircuitBreaker",
                        URI.create("forward:/fallbackroute")
                ))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> userFallback(){
        return route("fallbackroute")
                .route(RequestPredicates.path("/fallbackroute"),
                        req -> ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE)
                                .body("AlertService is Down"))
                .build();
    }
}
