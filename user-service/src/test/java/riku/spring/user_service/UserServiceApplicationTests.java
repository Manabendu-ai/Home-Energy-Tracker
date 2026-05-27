package riku.spring.user_service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import riku.spring.user_service.model.User;
import riku.spring.user_service.repo.UserRepo;

@Slf4j
@SpringBootTest
class UserServiceApplicationTests {

	@Autowired
	private UserRepo repo;
	private final int TOTAL_USERS = 20;

	@Test
	void contextLoads() {
	}

	@Disabled
	@Test
	void createUsers() {
		for (int i = 0; i <= TOTAL_USERS; i++) {
			User user = User.builder()
					.name("User " + i)
					.email("email@" + i)
					.alerting(i%2==0)
					.threshold(200.0+(i*1.3))
					.password("pass"+i)
					.build();
			repo.save(user);
			log.info("User repository Populated");
		}
	}
}
