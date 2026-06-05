package riku.spring.usage_service.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record UsageDTO(
        Long userId,
        List<DeviceResponse> devices
) {

}
