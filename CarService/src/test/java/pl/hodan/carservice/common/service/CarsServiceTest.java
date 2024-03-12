package pl.hodan.carservice.common.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import pl.hodan.carservice.common.entity.Car;
import pl.hodan.carservice.common.entity.Client;
import pl.hodan.carservice.common.entity.User;
import pl.hodan.carservice.common.entity.UserBasicInformation;
import pl.hodan.carservice.common.exception.CarNotFoundException;
import pl.hodan.carservice.common.exception.ClientNotFoundException;
import pl.hodan.carservice.common.repository.CarRepository;
import pl.hodan.carservice.common.vindecoder.controller.VinDecoderController;
import pl.hodan.carservice.common.vindecoder.model.VINDecoderModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarsServiceTest {
    @Mock
    private CarRepository carRepository;
    @Mock
    private ClientsService clientsService;
    @Mock
    private VinDecoderController vinDecoderController;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private CarsService carsService;
    private Car car;
    private Client client;
    private User user;

    @BeforeEach
    public void initialize(){
        car = new Car();
        car.setId(1L);
        car.setVIN("SDSDS77SD2SD7DSAS");
        car.setRegion("Europe");
        car.setCountry("Poland");
        car.setMake("Seat");
        car.setModel("Leon 2");
        car.setModelYear("2006");
        car.setBodyStyle("hatchback");
        car.setEngineType("gasoline 1.4");
        car.setFuelType("gasoline");
        car.setTransmission("4");
        car.setManufacturedIn("France");
        car.setBodyType("hatchback");
        car.setNumberOfDoors("3");
        car.setNumberOfSeats("5");

        client  = new Client();
        client.setId(1L);
        client.setName("Mariusz");
        client.setEmail("mariusz400@onet.pl");
        client.setAddress("Osieczany 321");
        client.setSurname("Kowalski");
        client.setPhoneNumber(322123221);





       UserBasicInformation userBasicInformation = new UserBasicInformation(1L, "Stefan", "Nowak", "Sułkowice 304", 532123221);
        user = new User(1L, "mariusz22@onet.pl", "1234", userBasicInformation);
    }

    @Test
    public void getCarsShouldReturnCarsWhenClientsExist(){
        // given
        Long userId = user.getId();
        Long clientId = client.getId();

        List<Client> clientList =Arrays.asList(client);
        when(clientsService.getClients(userId)).thenReturn(clientList);

        List<Car> expectedCars = Arrays.asList(car);
        when(carRepository.findCarsByClientId(clientId)).thenReturn(expectedCars);

        // when
        List<Car> actualCars = carsService.getCarsByUserIdAndClientId(userId, clientId);

        // then
        assertFalse(actualCars.isEmpty());
        assertEquals(expectedCars, actualCars);
    }
    @Test
    public void getCarsShouldThrowClientNotFoundExceptionWhenClientsListIsEmpty(){
        //given
        Long userId = user.getId();
        Long clientId = client.getId();

        List<Client> emptyClientsList = new ArrayList<>();
        when(clientsService.getClients(userId)).thenReturn(emptyClientsList);

        //when
        //then
        assertThrows(ClientNotFoundException.class, ()-> carsService.getCarsByUserIdAndClientId(userId,clientId));
    }
    @Test
    public void getCarShouldEmptyCarsListWhenCarsIsNotExist(){
        Long userId = user.getId();
        Long clientId = client.getId();

        List<Client> clientList =Arrays.asList(client);
        when(clientsService.getClients(userId)).thenReturn(clientList);

        List<Car> emptyCarsList = new ArrayList<>();
        when(carRepository.findCarsByClientId(clientId)).thenReturn(emptyCarsList);

        // when
        List<Car> actualCars = carsService.getCarsByUserIdAndClientId(userId, clientId);

        // then
        assertTrue(actualCars.isEmpty());
    }

    @Test
    public void getCarShouldReturnCarWhenClientAndCarExist(){
        //given
        car.setClient(client);
        Long carId = car.getId();
        when(carRepository.findById(carId)).thenReturn(Optional.of(car));

        Long userId = user.getId();
        List<Client> clientsList =Arrays.asList(client);
        when(clientsService.getClients(userId)).thenReturn(clientsList);


        //when
        Car result = carsService.getCarByUserIdAndCarId(userId,carId);
        //then
        assertNotNull(result);
        assertEquals(car,result);

    }
    @Test
    public void getCarShouldThrowCarNotFoundExceptionWhenClientNotExist(){
        //given
        car.setClient(client);
        Long carId = car.getId();
        when(carRepository.findById(carId)).thenReturn(Optional.of(car));

        Long userId = user.getId();
        List<Client> clientsEmptyList =new ArrayList<>();
        when(clientsService.getClients(userId)).thenReturn(clientsEmptyList);
        //when
        //then
        assertThrows(CarNotFoundException.class, ()-> carsService.getCarByUserIdAndCarId(userId,carId));
    }

    @Test
    public void addCarShouldReturnTrueWhenWeAddClient(){
        //given
        Long userId = user.getId();
        Long clientId = client.getId();
        String vin = "SDSDS77SD2SD7DSAS";

        List<Client> clientList = Arrays.asList(client);
        when(clientsService.getClients(userId)).thenReturn(clientList);

        when(clientsService.checkIfClientIdExist(clientId)).thenReturn(client);

        when(vinDecoderController.getDecodeVin(vin)).thenReturn(new VINDecoderModel());
        //when
        boolean result = carsService.addCar(userId,clientId,vin);
        //then
        assertTrue(result);
        verify(carRepository).save(any(Car.class));
    }
    @Test
    public void addCarShouldThrowClientNotFoundExceptionWhenClientListIsEmpty(){
        //given
        Long userId = user.getId();
        Long clientId = client.getId();
        String vin = "SDSDS77SD2SD7DSAS";

        List<Client> emptyList = new ArrayList<>();
        when(clientsService.getClients(userId)).thenReturn(emptyList);

        //when
        //then
        assertThrows(ClientNotFoundException.class, () -> carsService.addCar(userId,clientId,vin));

    }
    @Test
    public void addCarShouldThrowClientNotFoundExceptionWhenClientIsNotExist(){
        //given
        Long userId = user.getId();
        Long clientId = client.getId();
        String vin = "SDSDS77SD2SD7DSAS";

        List<Client> clientList = Arrays.asList(client);
        when(clientsService.getClients(userId)).thenReturn(clientList);

        when(clientsService.checkIfClientIdExist(clientId)).thenThrow(ClientNotFoundException.class);

        //when
        //then
        assertThrows(ClientNotFoundException.class, () -> carsService.addCar(userId,clientId,vin));


    }
    @Test
    void deleteCarShouldReturnTrueWhenCarExistAndClientExist() {
        // given
        car.setClient(client);
        Long carId = car.getId();
        when(carRepository.findById(carId)).thenReturn(Optional.of(car));

        Long userId = user.getId();
        List<Client> clientList = Arrays.asList(client);
        when(clientsService.getClients(userId)).thenReturn(clientList);


        // when
        boolean result = carsService.deleteCarWithClientIdByCarId(userId, carId);

        // then
        assertTrue(result);
        verify(carRepository).deleteById(anyLong()); // Sprawdź, czy metoda deleteById została wywołana na repozytorium
    }
    @Test
    public void deleteCarShouldThrowCarNotFoundExceptionWhenIsNotExist(){
        //given
        Long carId = car.getId();
        when(carRepository.findById(carId)).thenReturn(Optional.empty());

        Long userId = user.getId();
        List<Client> clientList = Arrays.asList(client);
        //when
        //then
        assertThrows(CarNotFoundException.class,() ->carsService.deleteCarWithClientIdByCarId(userId,carId));

    }
    @Test
    public void hasUSerAccessToCarShouldReturnTrueWhenUserHasAccessToCar(){
        //given
        car.setClient(client);
        Long carId = car.getId();
        when(carRepository.findById(carId)).thenReturn(Optional.of(car));

        Long userId = user.getId();
        List<Client> clientList = Arrays.asList(client);
        when(clientsService.getClients(userId)).thenReturn(clientList);

        //when
       boolean result =  carsService.hasUserAccessToCar(userId,carId);
        //then
        assertTrue(result);

    }
    @Test
    public void hasUSerAccessToCarShouldReturnFalseWhenUserHasNotAccessToCar(){
        //given

        Client client1 = new Client();
        client1.setId(2L);

        car.setClient(client1);
        Long carId = car.getId();
        when(carRepository.findById(carId)).thenReturn(Optional.of(car));

        Long userId = user.getId();
        List<Client> clientList = Arrays.asList(client);
        when(clientsService.getClients(userId)).thenReturn(clientList);

        //when
        boolean result =  carsService.hasUserAccessToCar(userId,carId);
        //then
        assertFalse(result);

    }



}