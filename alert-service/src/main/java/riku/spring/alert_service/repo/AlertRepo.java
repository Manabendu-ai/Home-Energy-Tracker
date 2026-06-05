package riku.spring.alert_service.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import riku.spring.alert_service.model.Alert;

@Repository
public interface AlertRepo extends JpaRepository<Alert, Long> {
}
