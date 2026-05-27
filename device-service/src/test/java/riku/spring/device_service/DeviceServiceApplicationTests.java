package riku.spring.device_service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import riku.spring.device_service.dto.DeviceRequest;
import riku.spring.device_service.model.Device;
import riku.spring.device_service.model.DeviceType;
import riku.spring.device_service.repo.DeviceRepo;
import riku.spring.device_service.service.DeviceService;

@Slf4j
@SpringBootTest
class DeviceServiceApplicationTests {

	private final int NUMBER_OF_DEVICES=100;
	private final int USERS=20;

	@Autowired
	private DeviceService service;

	@Test
	void contextLoads() {
	}


	@Disabled
	@Test
	void createDevice(){
		for(int i = 0; i<=NUMBER_OF_DEVICES; i++){
				var device = DeviceRequest.builder()
						.name("Device: "+i)
						.type(DeviceType.values()[i % DeviceType.values().length])
						.location("location: "+(i+1))
						.userId((long)  (i%USERS)+1)
						.build();
				service.create(device);
				log.info("Device repository Populated");
			}

		}
	}


