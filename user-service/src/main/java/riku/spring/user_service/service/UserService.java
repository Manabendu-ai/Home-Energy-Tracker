package riku.spring.user_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import riku.spring.user_service.dto.UserRequest;
import riku.spring.user_service.dto.UserResponse;
import riku.spring.user_service.model.User;
import riku.spring.user_service.repo.UserRepo;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo repo;

    public ResponseEntity<?> createuser(UserRequest req) {
        log.info("User Request: {}",req);
        if(repo.existsByEmail(req.getEmail())){
            return  new ResponseEntity<>("User With This Email Already Exists", HttpStatus.ALREADY_REPORTED);
        }
        return new ResponseEntity<>(toResponse(repo.save(toUser(req))), HttpStatus.CREATED);
    }

    public ResponseEntity<?> getUsers(){
        List<User> users = repo.findAll();
        if(users.isEmpty()){
            return new ResponseEntity<>("There are currently no users!", HttpStatus.OK);
        }
        List<UserResponse> userRes = new ArrayList<>();
        for(User u : users){
            userRes.add(toResponse(u));
        }
        return new ResponseEntity<>(userRes, HttpStatus.OK);
    }

    public ResponseEntity<?> getUserbyId(Long id){
        User user = repo.findById(id).orElse(null);
        if(user ==  null){
            return new ResponseEntity<>("User with ID:"+id+" not found!", HttpStatus.OK);
        }
        return new ResponseEntity<>(toResponse(user), HttpStatus.OK);
    }

    public ResponseEntity<?> updateUser(long id, UserRequest req) {
        User user = repo.findById(id).orElse(null);
        if(user == null){
            return new ResponseEntity<>("User with ID:"+id+" not found!", HttpStatus.NOT_FOUND);
        }
        user.setName(req.getName());
        user.setEmail(req.getEmail());
        user.setPassword(req.getPassword());
        user.setAlerting(req.isAlerting());
        user.setThreshold(req.getThreshold());
        return new ResponseEntity<>(toResponse(repo.save(user)), HttpStatus.OK);
    }

    public ResponseEntity<?> deleteUserById(long id){
        User user = repo.findById(id).orElse(null);
        if(user == null){
            return new ResponseEntity<>("User with ID:"+id+" not found!", HttpStatus.NOT_FOUND);
        }
        repo.deleteById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    public User toUser(UserRequest req){
        return User.builder()
                .name(req.getName())
                .email(req.getEmail())
                .password(req.getPassword())
                .alerting(req.isAlerting())
                .threshold(req.getThreshold())
                .build();
    }

    public UserResponse toResponse(User user){
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .alerting(user.isAlerting())
                .threshold(user.getThreshold())
                .build();
    }


}
