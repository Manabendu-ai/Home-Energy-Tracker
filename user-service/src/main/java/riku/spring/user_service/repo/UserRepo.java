package riku.spring.user_service.repo;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import riku.spring.user_service.model.User;

import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
}
