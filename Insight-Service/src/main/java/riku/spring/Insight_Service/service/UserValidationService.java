package riku.spring.Insight_Service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class UserValidationService {

    private final WebClient webClient;

    public Boolean isValidUser(Long userId){
        return Boolean.TRUE.equals(
                webClient.get()
                        .uri("http://localhost:8081/api/user/validate/" + userId)
                        .retrieve()
                        .bodyToMono(Boolean.class)
                        .block()
        );
    }
}
