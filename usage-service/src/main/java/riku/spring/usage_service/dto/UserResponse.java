package riku.spring.usage_service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private boolean alerting;
    private double threshold;
}
