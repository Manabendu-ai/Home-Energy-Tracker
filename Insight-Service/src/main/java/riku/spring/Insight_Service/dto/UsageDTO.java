package riku.spring.Insight_Service.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record UsageDTO(
        Long userId,
        List<DeviceDTO> devices
) {
}
