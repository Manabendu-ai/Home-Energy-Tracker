package riku.spring.user_service.controller;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import riku.spring.user_service.dto.UserRequest;
import riku.spring.user_service.service.UserService;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody UserRequest req){
        return service.createuser(req);
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getALlUsers(){
        return service.getUsers();
    }

    @GetMapping("/validate/{id}")
    public ResponseEntity<Boolean> validateUserById(@PathVariable Long id){
        return ResponseEntity.ok(service.userValidationById(id));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getUserById(@PathVariable long id){
        return service.getUserbyId(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable long id, @RequestBody UserRequest req){
        return service.updateUser(id, req);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable long id){
        return service.deleteUserById(id);
    }
}
