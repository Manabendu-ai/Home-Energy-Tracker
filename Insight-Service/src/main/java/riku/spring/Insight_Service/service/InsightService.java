package riku.spring.Insight_Service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import riku.spring.Insight_Service.dto.DeviceDTO;
import riku.spring.Insight_Service.dto.InsightDTO;
import riku.spring.Insight_Service.dto.UsageDTO;

@Slf4j
@Service
@RequiredArgsConstructor
public class InsightService {
    private final UserValidationService userValidationService;
    private final UsageService usageService;
    private final OllamaChatModel ollamaChatModel;
//    public ResponseEntity<?> getSavingTips(Long userId) {
//        if(userValidationService.isValidUser(userId)){
//
//        }
//        return null;
//    }

    public ResponseEntity<?> getOverview(Long userId) {
        if(userValidationService.isValidUser(userId)){
            final UsageDTO usageData = usageService.getXDaysUsageByUserId(userId,3);

            double totalUsage = usageData.devices().stream()
                            .mapToDouble(DeviceDTO::energyConsumed).sum();

            log.info("calling Ollama for userId {} with total Usage {}",
                    userId, totalUsage
                    );
            String prompt = new StringBuilder()
                    .append("Analyse the following energy usage data and provide a " +
                            "concise overview with actionable insights.")
                    .append("This data is the aggregate data for the past 3 days.")
                    .append("Usage Data: \n")
                    .append(usageData.devices())
                    .toString();

            ChatResponse response = ollamaChatModel.call(
                    Prompt.builder()
                            .content(prompt)
                            .build()
            );
            return new ResponseEntity<>(
                    InsightDTO.builder()
                            .userId(userId)
                            .tips(response.getResult().getOutput().getText())
                            .energyUsage(totalUsage)
                            .build(),
                    HttpStatus.OK
            );
        }
        return new ResponseEntity<>(
                "Invalid User ID",
                HttpStatus.NOT_FOUND
        );
    }
}
