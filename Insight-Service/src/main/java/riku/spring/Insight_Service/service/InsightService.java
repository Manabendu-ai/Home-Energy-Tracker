package riku.spring.Insight_Service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InsightService {
    private final UserValidationService userValidationService;
    public ResponseEntity<?> getSavingTips(Long userId) {
        if(userValidationService.isValidUser(userId)){

        }
        return null;
    }

    public ResponseEntity<?> getOverview(Long userId) {
        if(userValidationService.isValidUser(userId)){

        }
        return null;
    }
}
