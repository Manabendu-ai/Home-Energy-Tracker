package riku.spring.ingestion_service.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class DeviceValidationService {

    private final WebClient webClient;

    public boolean validateDeviceById(long id){
        return Boolean.TRUE.equals(
                webClient.get()
                        .uri("http://localhost:8082/api/device/validate/" + id)
                        .retrieve()
                        .bodyToMono(Boolean.class)
                        .block()
        );
    }
}
