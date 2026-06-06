package riku.spring.usage_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import riku.spring.usage_service.service.UsageService;

@RestController
@RequestMapping("/api/usage")
@RequiredArgsConstructor
public class UsageController {

    private final UsageService usageService;

    @GetMapping("/test")
    public String test() {
        return "Usage Service Working";
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserDeviceUsage(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "3") int days
    ){
        return usageService.getXDaysUsageForUser(userId, days);
    }

}
