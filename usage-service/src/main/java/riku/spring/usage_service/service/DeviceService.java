package riku.spring.usage_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import riku.spring.usage_service.dto.DeviceResponse;

@Service
@RequiredArgsConstructor
public class DeviceService {

    private final WebClient webClient;

    public DeviceResponse getDeviceDto(Long id){
        try {
            return webClient.get()
                    .uri("http://localhost:8082/api/device/get/" + id)
                    .retrieve()
                    .bodyToMono(DeviceResponse.class)
                    .block();
        } catch (Exception e){
            return null;
        }
    }
}
