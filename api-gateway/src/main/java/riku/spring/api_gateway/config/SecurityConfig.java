package riku.spring.api_gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Value("${keycloak.auth.jwk-set-uri}")
    private String uri;

    @Value("${security.excluded.url}")
    private String[] excludedUrls;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                x -> x
                        .requestMatchers("/api/user/create", "/actuator/**").permitAll()
                        .anyRequest().authenticated()
        )

                .oauth2ResourceServer(
                        oauth2 -> oauth2.jwt(Customizer.withDefaults())
                ).build();
    }

    @Bean
    public JwtDecoder jwtDecoder(){
        return NimbusJwtDecoder.withJwkSetUri(uri).build();
    }
}
