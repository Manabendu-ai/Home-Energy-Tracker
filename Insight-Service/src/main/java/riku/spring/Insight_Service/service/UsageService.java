package riku.spring.Insight_Service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import riku.spring.Insight_Service.dto.UsageDTO;

@Service
@RequiredArgsConstructor
public class UsageService {

    private final WebClient webClient;

    public UsageDTO getXDaysUsageByUserId(Long userId,int days){

        try{
            return webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/usage/{userId}")
                            .queryParam("days", days)
                            .build(userId))
                    .retrieve()
                    .bodyToMono(UsageDTO.class)
                    .block();
        } catch (Exception e) {
            return null;
        }

    }
}
