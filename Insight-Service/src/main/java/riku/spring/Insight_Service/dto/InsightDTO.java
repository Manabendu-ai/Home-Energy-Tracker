package riku.spring.Insight_Service.dto;

import lombok.Builder;

@Builder
public record InsightDTO(
        Long userId,
        String tips,
        double energyUsage
) {
}
