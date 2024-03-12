package pl.hodan.carservice.common.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.hodan.carservice.auth.services.AuthenticationService;
import pl.hodan.carservice.common.entity.Car;
import pl.hodan.carservice.common.entity.User;
import pl.hodan.carservice.common.service.CarsService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarControllerTest {
    @Mock
    private CarsService carsService;
    @Mock
    private AuthenticationService authenticationService;
    @InjectMocks
    private CarController carController;

    private User user;
    @BeforeEach
    public void initializeUserId(){
        user = new User();
        user.setId(1L);
    }
    @Test
    void getCarsShouldReturnCars() {
        // given
        Long userId = user.getId();
        Long clientId = 2L;

        Car car = new Car();
        List<Car> expectedCars = List.of(car);
        when(authenticationService.getCurrentUserId()).thenReturn(userId);
        when(carsService.getCarsByUserIdAndClientId(userId, clientId)).thenReturn(expectedCars);
        // when
        ResponseEntity<List<Car>> response = carController.getCars(clientId);
        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedCars, response.getBody());
    }
    @Test
    void getCarByCarIdShouldReturnCar() {
        // given
        Long userId = user.getId();
        Long carId = 2L;
        Car expectedCar = new Car();
        when(authenticationService.getCurrentUserId()).thenReturn(userId);
        when(carsService.getCarByUserIdAndCarId(userId, carId)).thenReturn(expectedCar);
        // when
        ResponseEntity<Car> response = carController.getCarByCarId(carId);
        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedCar, response.getBody());
    }
    @Test
    void addCarShouldReturnCreatedWhenSuccessful() {
        // given
        Long userId = user.getId();
        Long clientId = 2L;
        String vin = "1HGBH41JXMN109186";
        when(authenticationService.getCurrentUserId()).thenReturn(userId);
        when(carsService.addCar(userId, clientId, vin)).thenReturn(true);
        // when
        ResponseEntity<String> response = carController.addCar(clientId, vin);
        // then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }
    @Test
    void addCarShouldReturnBadRequestWhenUnsuccessful() {
        // given
        Long userId = user.getId();
        Long clientId = 2L;
        String vin = "1HGBH41JXMN109186";
        when(authenticationService.getCurrentUserId()).thenReturn(userId);
        when(carsService.addCar(userId, clientId, vin)).thenReturn(false);
        // when
        ResponseEntity<String> response = carController.addCar(clientId, vin);
        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
    @Test
    void deleteCarShouldReturnOkWhenSuccessful() {
        // given
        Long userId = user.getId();
        Long carId = 3L;
        when(authenticationService.getCurrentUserId()).thenReturn(userId);
        when(carsService.deleteCarWithClientIdByCarId(userId, carId)).thenReturn(true);
        // when
        ResponseEntity<String> response = carController.deleteCar(carId);
        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    @Test
    void deleteCarShouldReturnNotFoundWhenUnsuccessful() {
        // given
        Long userId =user.getId();
        Long carId = 3L;
        when(authenticationService.getCurrentUserId()).thenReturn(userId);
        when(carsService.deleteCarWithClientIdByCarId(userId, carId)).thenReturn(false);
        // when
        ResponseEntity<String> response = carController.deleteCar(carId);
        // then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

}