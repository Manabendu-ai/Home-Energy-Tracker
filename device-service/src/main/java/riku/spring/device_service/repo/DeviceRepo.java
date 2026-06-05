package riku.spring.device_service.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import riku.spring.device_service.model.Device;

import java.util.List;

@Repository
public interface DeviceRepo extends JpaRepository<Device, Long> {
    List<Device> findAllByUserId(Long userId);
}
