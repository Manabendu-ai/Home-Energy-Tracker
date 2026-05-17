package riku.spring.device_service.service;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import riku.spring.device_service.dto.DeviceRequest;
import riku.spring.device_service.dto.DeviceResponse;
import riku.spring.device_service.model.Device;
import riku.spring.device_service.repo.DeviceRepo;

@Service
@RequiredArgsConstructor
public class DeviceService {

    private final DeviceRepo repo;
    private final UserValidationService validationService;

    public ResponseEntity<?> getDeviceById(Long id) {
        Device device = repo.findById(id)
                .orElse(null);

        if(device == null){
            return new ResponseEntity<>("Device with id: "+id+" Not Found!", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(toResponse(device), HttpStatus.OK);
    }

    public Device toDevice(DeviceRequest req){
        return Device.builder()
                .name(req.getName())
                .type(req.getType())
                .location(req.getLocation())
                .userId(req.getUserId())
                .build();
    }

    public DeviceResponse toResponse(Device device){
        return DeviceResponse.builder()
                .id(device.getId())
                .name(device.getName())
                .type(device.getType())
                .location(device.getLocation())
                .userId(device.getUserId())
                .build();
    }

    public ResponseEntity<?> create(DeviceRequest request) {
        if(validationService.isValidUser(request.getUserId())){
            return new ResponseEntity<>(toResponse(repo.save(toDevice(request))), HttpStatus.OK);
        }
        return new ResponseEntity<>("User with id: "+request.getUserId()+" Not Found!", HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<?> update(Long id, DeviceRequest request){
        Device device = repo.findById(id)
                .orElse(null);

        if(device == null){
            return new ResponseEntity<>("Device with id: "+id+" Not Found!", HttpStatus.NOT_FOUND);
        }

        device.setName(request.getName());
        device.setType(request.getType());
        device.setLocation(request.getLocation());
        device.setUserId(request.getUserId());

        return new ResponseEntity<>(toResponse(repo.save(device)), HttpStatus.OK);
    }

    public ResponseEntity<?> delete(Long id){
        Device device = repo.findById(id)
                .orElse(null);

        if(device == null){
            return new ResponseEntity<>("Device with id: "+id+" Not Found!", HttpStatus.NOT_FOUND);
        }
        repo.delete(device);
        return  new ResponseEntity<>(toResponse(device), HttpStatus.OK);
    }
}
