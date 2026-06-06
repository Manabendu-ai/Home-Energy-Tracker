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
public class UsageService {
    @Bean
    public RouterFunction<ServerResponse> usageRoute(){
        return route("usage-service")
                .route(RequestPredicates.path("/api/usage/**"), http())
                .before(uri("http://localhost:8084"))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker(
                        "UsageServiceCircuitBreaker",
                        URI.create("forward:/fallbackroute")
                ))
                .build();
    }
    @Bean
    public RouterFunction<ServerResponse> usageFallback(){
        return route("fallbackroute")
                .route(RequestPredicates.path("/fallbackroute"),
                        req -> ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE)
                                .body("UsageService is Down"))
                .build();
    }
}
