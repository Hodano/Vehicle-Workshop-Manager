package pl.hodan.carservice.common.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.hodan.carservice.auth.services.AuthenticationService;
import pl.hodan.carservice.common.entity.Car;
import pl.hodan.carservice.common.service.CarsService;

import java.util.List;

@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
@RestController
@RequestMapping("/cars-service")
public class CarController {
    private final CarsService carsService;
    private final AuthenticationService authenticationService;

    public CarController(CarsService carsService, AuthenticationService authenticationService) {
        this.carsService = carsService;
        this.authenticationService = authenticationService;
    }

    @GetMapping("/cars")
    public ResponseEntity<List<Car>> getCars(@RequestParam Long clientId) {
        Long userId = authenticationService.getCurrentUserId();

        List<Car> cars = carsService.getCarsByUserIdAndClientId(userId, clientId);
        return ResponseEntity.ok(cars);
    }

    @GetMapping("/car")
    public ResponseEntity<Car> getCarByCarId(@RequestParam Long carId) {
        Long userId = authenticationService.getCurrentUserId();
        Car car = carsService.getCarByUserIdAndCarId(userId, carId);
        return ResponseEntity.ok(car);
    }

    @PostMapping("/add-car")
    public ResponseEntity<String> addCar(@RequestParam Long clientId, @RequestParam String vin) {
        Long userId = authenticationService.getCurrentUserId();

        if (carsService.addCar(userId, clientId, vin))
            return new ResponseEntity<>(HttpStatus.CREATED);
        return new ResponseEntity<>("Car could not be added", HttpStatus.BAD_REQUEST);
    }


    @DeleteMapping("/delete-car")
    public ResponseEntity<String> deleteCar(@RequestParam Long carId) {
        Long userId = authenticationService.getCurrentUserId();

        if (carsService.deleteCarWithClientIdByCarId(userId, carId))
            return new ResponseEntity<>("Car deleted", HttpStatus.OK);
        return new ResponseEntity<>("Car not found or could not be deleted", HttpStatus.NOT_FOUND);
    }
}
