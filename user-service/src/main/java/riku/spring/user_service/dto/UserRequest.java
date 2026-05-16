package riku.spring.user_service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRequest {
    private String name;
    private String email;
    private String password;
    private boolean alerting;
    private double threshold;
}
