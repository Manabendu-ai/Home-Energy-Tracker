package riku.spring.usage_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceEnergyUsage{
        Long deviceId;
        double energyConsumed;
        Long userId;
}
