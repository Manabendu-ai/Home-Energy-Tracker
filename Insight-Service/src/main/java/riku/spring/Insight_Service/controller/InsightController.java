package riku.spring.Insight_Service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import riku.spring.Insight_Service.service.InsightService;

@RestController
@RequestMapping("/api/insight")
@RequiredArgsConstructor
public class InsightController {

    private final InsightService service;

    @GetMapping("/saving-tips/{userId}")
    public ResponseEntity<?> getSavingTips(@PathVariable Long userId){
        return service.getSavingTips(userId);
    }

    @GetMapping("/overview/{userId}")
    public ResponseEntity<?> getOverview(@PathVariable Long userId){
        return service.getOverview(userId);
    }
}
