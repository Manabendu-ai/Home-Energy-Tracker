package riku.spring.ingestion_service.simulation;


import jakarta.annotation.PreDestroy;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import riku.spring.ingestion_service.dto.EnergyDTO;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@Component
@Slf4j
public class ParallelDataSimulator implements CommandLineRunner {

    private final ExecutorService executorService;
    private final RestTemplate restTemplate = new RestTemplate();
    private final Random random = new Random();

    @Value("${simulation.parallel-thread}")
    private int parallelThreads;

    @Value("${simulation.ingestion-endpoint}")
    private String ingestionEndPoint;

    @Value("${simulation.request-ms}")
    private int requestPerInterval;

    public ParallelDataSimulator() {
        this.executorService = Executors.newCachedThreadPool();
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("ParallelDataSimulator Started....");
        ((ThreadPoolExecutor)executorService).setCorePoolSize(parallelThreads);
    }

    @Scheduled(fixedRateString = "${simulation.interval-ms}")
    public void sendMockData(){
        int batchSize = requestPerInterval / parallelThreads;
        int remainder = requestPerInterval % parallelThreads;

        for (int i = 0; i < parallelThreads; i++) {
            int requestsForThread = batchSize + (i < remainder ? 1 : 0);
            executorService.submit(() -> {
                for (int j = 0; j < requestsForThread; j++) {
                    EnergyDTO dto = EnergyDTO.builder()
                            .deviceId(random.nextLong(1, 6))
                            .energyConsumed(Math.round(random.nextDouble(0.0, 2.0) * 100.0) / 100.0)
                            .timestamp(LocalDateTime.now()
                                    .atZone(ZoneId.systemDefault()).toInstant())
                            .build();
                    try {
                        HttpHeaders headers = new HttpHeaders();
                        headers.setContentType(MediaType.APPLICATION_JSON);
                        HttpEntity<EnergyDTO> request = new HttpEntity<>(dto, headers);
                        restTemplate.postForEntity(ingestionEndPoint, request, Void.class);
                        log.info("Sent mock data: {}", dto);
                    } catch (Exception e) {
                        log.error("Failed to send data: {}", e.getMessage());
                    }
                }
            });
        }

    }

    @PreDestroy
    public void shutDown(){
        executorService.shutdown();
        log.info("ParallelDataSimulator shut down...");
    }
}
