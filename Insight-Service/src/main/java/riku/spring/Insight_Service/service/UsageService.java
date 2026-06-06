package riku.spring.Insight_Service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import riku.spring.Insight_Service.dto.UsageDTO;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsageService {

    private final WebClient webClient;

    public UsageDTO getXDaysUsageByUserId(Long userId, int days) {

        try {
            UsageDTO response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/usage/{userId}")
                            .queryParam("days", days)
                            .build(userId))
                    .retrieve()
                    .bodyToMono(UsageDTO.class)
                    .block();

            log.info("Usage Response: {}", response);

            return response;

        } catch (Exception e) {

            log.error("Error fetching usage data for user {}", userId, e);

            throw e;
        }
    }
}