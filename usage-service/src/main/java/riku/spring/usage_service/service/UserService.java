package riku.spring.usage_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import riku.spring.usage_service.dto.UserResponse;

@Service
@RequiredArgsConstructor
public class UserService {

    private final WebClient webClient;

    public UserResponse getUserById(long id){

        try {
            return webClient.get()
                    .uri("http://localhost:8081/api/user/get/" + id)
                    .retrieve()
                    .bodyToMono(UserResponse.class)
                    .block();
        } catch (Exception e) {
            return null;
        }

    }
}
