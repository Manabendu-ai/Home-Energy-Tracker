package riku.spring.ingestion_service.simulation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import riku.spring.ingestion_service.dto.EnergyDTO;
import org.springframework.http.HttpHeaders;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Random;

@Slf4j
@Component
@RequiredArgsConstructor
public class ContinuousDataSimulator implements CommandLineRunner {

    private final RestTemplate restTemplate = new RestTemplate();
    private final Random random = new Random();

    @Value("${simulation.request-ms}")
    private int requestPerInterval;

    @Value("${simulation.ingestion-endpoint}")
    private String ingestionEndPoint;

    @Override
    public void run(String... args){
        log.info("ContinuousDataSimulator started.....");
    }

    @Scheduled(fixedRateString = "${simulation.interval-ms}")
    public void sendMockData() {
        for (int i = 0; i < requestPerInterval; i++) {
            EnergyDTO dto = EnergyDTO.builder()
                    .deviceId(random.nextLong(1, 6))
                    .energyConsumed(Math.round(random.nextDouble(0.0, 10.0) * 100.0) / 100.0)
                    .timestamp(
                            LocalDateTime.now()
                                    .atZone(ZoneId.systemDefault())
                                    .toInstant()
                    )
                    .build();

            try{
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_PROBLEM_JSON);

                HttpEntity<EnergyDTO> req = new HttpEntity<>(dto, headers);
                restTemplate.postForEntity(ingestionEndPoint,req, Void.class);

                log.info("Sent Mock Data : {}", dto);
            } catch (Exception e){
                log.error("Failed to send data: {}", e.getMessage());
            }
        }
    }
}
