package riku.spring.device_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import riku.spring.device_service.dto.DeviceRequest;
import riku.spring.device_service.service.DeviceService;

@RestController
@RequestMapping("/api/device")
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceService service;

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getDeviceById(@PathVariable Long id){
        return service.getDeviceById(id);
    }

    @GetMapping("/validate/{id}")
    public ResponseEntity<Boolean> validateDeviceById(@PathVariable Long id){
        return ResponseEntity.ok(service.validateDeviceById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<?> createDevice(@RequestBody DeviceRequest request){
        return service.create(request);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<?> updateDevice(@PathVariable Long id, @RequestBody DeviceRequest request){
        return service.update(id, request);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteDevice(@PathVariable Long id){
        return service.delete(id);
    }
}
