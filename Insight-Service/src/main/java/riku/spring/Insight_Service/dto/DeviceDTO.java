package riku.spring.Insight_Service.dto;

import lombok.Builder;

@Builder
public record DeviceDTO(
        Long userId,
        String name,
        String type,
        String location,
        double energyConsumed
) {
}
