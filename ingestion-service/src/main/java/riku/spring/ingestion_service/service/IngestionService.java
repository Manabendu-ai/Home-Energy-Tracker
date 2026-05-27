package riku.spring.ingestion_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import riku.spring.ingestion_service.dto.EnergyDTO;
import riku.spring.kafka.event.EnergyUsageEvent;

@Slf4j
@Service
@RequiredArgsConstructor
public class IngestionService {

    private final KafkaTemplate<String, EnergyUsageEvent> kafkaTemplate;
    private final DeviceValidationService deviceValidationService;

    public ResponseEntity<?>  ingestEnergyUsage(EnergyDTO energyDTO) {
        if(deviceValidationService.validateDeviceById(energyDTO.deviceId())) {
            EnergyUsageEvent event = EnergyUsageEvent.builder()
                    .deviceId(energyDTO.deviceId())
                    .energyConsumed(energyDTO.energyConsumed())
                    .timestamp(energyDTO.timestamp())
                    .build();

            kafkaTemplate.send("energy-usage", event);
            log.info("Ingested Energy Usage Event: {}", event);
            return new ResponseEntity<>(event, HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Device with Device Id:"+energyDTO.deviceId()+" not found!", HttpStatus.NOT_FOUND);
    }
}
