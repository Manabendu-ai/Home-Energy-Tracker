package riku.spring.device_service.dto;

import lombok.Builder;
import lombok.Data;
import riku.spring.device_service.model.DeviceType;

@Data
@Builder
public class DeviceResponse {
    private Long id;
    private String name;
    private DeviceType type;
    private String location;
    private Long userId;
}
