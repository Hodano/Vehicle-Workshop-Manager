package pl.hodan.carservice.common.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import pl.hodan.carservice.common.entity.Car;
import pl.hodan.carservice.common.entity.History;
import pl.hodan.carservice.common.entity.User;
import pl.hodan.carservice.common.entity.UserBasicInformation;
import pl.hodan.carservice.common.exception.CarNotFoundException;
import pl.hodan.carservice.common.exception.HistoryNotFoundException;
import pl.hodan.carservice.common.repository.HistoryRepository;


import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class HistoriesServiceTest {
    @Mock
    private HistoryRepository historyRepository;
    @Mock
    private CarsService carsService;
    @InjectMocks
    private HistoriesService historiesService;
    private History history;
    private User user;

    private Car car;

    @BeforeEach
    public void initialize(){
        history = new History();
        history.setDescriptionHistory("replacement of brake pads");
        Date date = new Date(1640995200000L);
        history.setDateOfHistoryCar(date);

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


        UserBasicInformation userBasicInformation = new UserBasicInformation(1L, "Stefan", "Nowak", "Sułkowice 304", 532123221);
        user = new User(1L, "mariusz22@onet.pl", "1234", userBasicInformation);
    }
    @Test
    public void getHistoriesShouldReturnListOfHistoriesWhenCarExistAndUserHasAccess(){
        //given
        Long userId = user.getId();
        Long carId = car.getId();

        when(carsService.checkIfCarIdExist(carId)).thenReturn(car);
        when(carsService.hasUserAccessToCar(userId,carId)).thenReturn(true);

        List<History> historyList = Arrays.asList(history);
        when(historyRepository.findHistoriesByCarId(carId)).thenReturn(historyList);
        //when
        List<History> actualList = historiesService.getHistoriesByCarId(userId,carId);
        //then
        assertEquals(historyList,actualList);
        assertNotNull(actualList);
        verify(historyRepository).findHistoriesByCarId(anyLong());

    }
    @Test
    public void getHistoriesShouldThrowCarNotFoundExceptionWHenCarIsNotExist(){
        //given
        Long userId = user.getId();
        Long carId = car.getId();

        when(carsService.checkIfCarIdExist(carId)).thenThrow(CarNotFoundException.class);

        //when
        //then
        assertThrows(CarNotFoundException.class, ()->historiesService.getHistoriesByCarId(userId,carId));
    }
    @Test
    public void getGHistoriesShouldThrowAccessDeniedExceptionWhereNoUserAccessToTheCar(){
        //given
        Long userId = user.getId();
        Long carId = car.getId();

        when(carsService.checkIfCarIdExist(carId)).thenReturn(car);
        when(carsService.hasUserAccessToCar(userId,carId)).thenReturn(false);

        //when
        //then
        assertThrows(AccessDeniedException.class, ()->historiesService.getHistoriesByCarId(userId,carId));
    }
    @Test
    public void getHistoryByIdShouldReturnHistoryWhenCarAndHistoryExistAndUserHasAccess(){
        //given
        Long historyId = history.getId();
        history.setCar(car);
        Long carId = car.getId();
        when(historyRepository.findById(historyId)).thenReturn(Optional.of(history));

        Long userId = user.getId();
        when(carsService.hasUserAccessToCar(userId,carId)).thenReturn(true);
        //when
        History actual = historiesService.getHistoryByHistoryId(userId,historyId);
        //then
        assertEquals(history,actual);
        assertNotNull(actual);
    }
    @Test
    public void getHistoryShouldThrowNotFoundHistoryWhenHistoryIsNotExist(){
        //given
        Long userId = user.getId();
        Long historyId = history.getId();
        history.setCar(car);
        when(historyRepository.findById(historyId)).thenReturn(Optional.empty());
        //when
        //then
        assertThrows(HistoryNotFoundException.class, ()->historiesService.getHistoryByHistoryId(userId,historyId));
    }
    @Test
    public void getHistoryShouldThrowCarNotFoundWhenCarIsNotExist(){
        //given
        Long userId = user.getId();
        Long historyId = history.getId();
        history.setCar(car);
        Long carId = car.getId();
        when(historyRepository.findById(historyId)).thenReturn(Optional.of(history));

        when(carsService.checkIfCarIdExist(carId)).thenThrow(CarNotFoundException.class);
        //when
        //then
        assertThrows(CarNotFoundException.class,()->historiesService.getHistoryByHistoryId(userId,historyId ));
    }
    @Test
    public void getHistoryShouldThrowAccessDeniedExceptionWhenNoUserAccessToTheCar(){
        //given
        Long historyId = history.getId();
        history.setCar(car);
        Long carId = car.getId();
        when(historyRepository.findById(historyId)).thenReturn(Optional.of(history));

        Long userId = user.getId();
        when(carsService.hasUserAccessToCar(userId,carId)).thenReturn(false);
        //when
        //then
        assertThrows(AccessDeniedException.class, ()-> historiesService.getHistoryByHistoryId(userId,historyId));
    }
    @Test
    public void addHistoryShouldReturnTrueWhenCarAndHistoryExistAndUserHasAccess(){
        //given

        Long carId = car.getId();
        when(carsService.checkIfCarIdExist(carId)).thenReturn(car);

        Long userId = user.getId();
        when(carsService.hasUserAccessToCar(userId,carId)).thenReturn(true);
        //when
        boolean actual = historiesService.addHistory(userId,carId,history);
        //then
        assertTrue(actual);
        verify(historyRepository).save(any(History.class));

    }
    @Test
    public void addHistoryShouldThrowAccessDeniedExceptionWhenNotUserAccessToTheCar(){
        //given
        Long carId = car.getId();
        when(carsService.checkIfCarIdExist(carId)).thenReturn(car);

        Long userId = user.getId();
        when(carsService.hasUserAccessToCar(userId,carId)).thenReturn(false);
        //when
        //then
        assertThrows(AccessDeniedException.class, ()-> historiesService.addHistory(userId,carId,history));


    }
    @Test
    public void modifyHistoryShouldReturnTrueWhenHistoryAndCarExistAndUserHasAccessToCar(){
        //given
        History newHistory = new History();
        Long historyId = history.getId();
        history.setCar(car);
        Long carId = car.getId();
        when(historyRepository.findById(historyId)).thenReturn(Optional.of(history));
        when(carsService.checkIfCarIdExist(carId)).thenReturn(car);
        Long userId = user.getId();
        when(carsService.hasUserAccessToCar(userId,carId)).thenReturn(true);
        //when
        boolean actual = historiesService.modifyHistoryWithCarIdByHistoryId(userId,historyId,newHistory);
        //then
        assertTrue(actual);
        verify(historyRepository).save(any(History.class));
    }
    @Test
    public void modifyHistoryShouldThrowHistoryNotFoundExceptionWhenHistoryIsNotExist(){
        //given
        Long userId = user.getId();
        History newHistory = new History();
        Long historyId = history.getId();
        when(historyRepository.findById(historyId)).thenReturn(Optional.empty());

        //when
        //then
        assertThrows(HistoryNotFoundException.class,()->historiesService.modifyHistoryWithCarIdByHistoryId(userId,historyId,newHistory));
    }
    @Test
    public void modifyHistoryShouldThrowAccessDeniedWhenUserNotAccessToCar(){
        //given
        Long userId = user.getId();
        History newHistory = new History();
        Long historyId = history.getId();
        history.setCar(car);

        Long carId = car.getId();
        when(historyRepository.findById(historyId)).thenReturn(Optional.of(history));
        when(carsService.hasUserAccessToCar(userId,carId)).thenReturn(false);
        //when
        //then
        assertThrows(AccessDeniedException.class,()->historiesService.modifyHistoryWithCarIdByHistoryId(userId,historyId,newHistory));

    }
    @Test
    public void deleteHistoryShouldReturnTrueWhenHistoryAndCarIsExistAndUserHasAccessToCar(){
        //given
        Long userId = user.getId();
        Long historyId = history.getId();
        history.setCar(car);
        when(historyRepository.findById(historyId)).thenReturn(Optional.of(history));
        Long carId = car.getId();

        when(carsService.hasUserAccessToCar(userId,carId)).thenReturn(true);
        //when
        boolean actual = historiesService.deleteHistoryByHistoryId(userId,historyId);
        //then
        assertTrue(actual);
    }
    @Test
    public void deleteHistoryShouldThrowAccessDeniedWhenUserNotAccessToCar(){
        //given
        Long userId = user.getId();

        Long historyId = history.getId();
        history.setCar(car);

        Long carId = car.getId();
        when(historyRepository.findById(historyId)).thenReturn(Optional.of(history));
        when(carsService.hasUserAccessToCar(userId,carId)).thenReturn(false);
        //when
        //then
        assertThrows(AccessDeniedException.class,()->historiesService.deleteHistoryByHistoryId(userId,historyId));
    }
    @Test
    public void checkIfShouldReturnHistoryWHenExist(){
        //given
        Long historyId = history.getId();

        when(historyRepository.findById(historyId)).thenReturn(Optional.of(history));
        //when
        History actual = historiesService.checkIfHistoryIdExist(historyId);
        //then
        assertEquals(history,actual);
        assertNotNull(actual);


    }
    @Test
    public void checkIfShouldThrowHistoryNotFoundExceptionWhenHistoryIsNotExist(){
        //given
        Long historyId = history.getId();

        when(historyRepository.findById(historyId)).thenReturn(Optional.empty());
        //when
        //then
        assertThrows(HistoryNotFoundException.class,()->historiesService.checkIfHistoryIdExist(historyId));
    }


}