package riku.spring.ingestion_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import riku.spring.ingestion_service.dto.EnergyDTO;
import riku.spring.ingestion_service.service.IngestionService;

@RestController
@RequestMapping("/api/ingestion")
@RequiredArgsConstructor
public class IngestionController {

    private final IngestionService service;

    @PostMapping("/create")
    public ResponseEntity<?> IngestData(@RequestBody EnergyDTO energyDTO){
        return service.ingestEnergyUsage(energyDTO);
    }
}
