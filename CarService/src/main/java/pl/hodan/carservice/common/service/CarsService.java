package pl.hodan.carservice.common.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pl.hodan.carservice.common.entity.Car;
import pl.hodan.carservice.common.entity.Client;
import pl.hodan.carservice.common.exception.CarNotFoundException;
import pl.hodan.carservice.common.exception.ClientNotFoundException;
import pl.hodan.carservice.common.repository.CarRepository;
import pl.hodan.carservice.common.vindecoder.controller.VinDecoderController;

import java.util.List;
import java.util.Optional;

@Service
public class CarsService {
    private final CarRepository carRepository;
    private final ClientsService clientsService;
    private final VinDecoderController vinDecoderController;

    private final ModelMapper modelMapper;


    public CarsService(CarRepository carRepository, ClientsService clientsService, VinDecoderController vinDecoderController, ModelMapper modelMapper) {
        this.carRepository = carRepository;
        this.clientsService = clientsService;
        this.vinDecoderController = vinDecoderController;
        this.modelMapper = modelMapper;
    }

    public List<Car> getCarsByUserIdAndClientId(Long userID, Long clientId) {


        List<Client> clients = clientsService.getClients(userID);

        return clients.stream()
                .map(Client::getId)
                .filter(id -> id.equals(clientId))
                .findFirst()
                .map(carRepository::findCarsByClientId)
                .orElseThrow(() -> new ClientNotFoundException("Client not found"));
    }



    public Car getCarByUserIdAndCarId(Long userId, Long carId) {

        return Optional.ofNullable(checkIfCarIdExist(carId))
                .filter(car -> clientsService.getClients(userId).stream()
                        .anyMatch(client -> client.getId().equals(car.getClient().getId())))
                .orElseThrow(() -> new CarNotFoundException("Car not found"));

    }

    public boolean addCar(Long userId, Long clientId, String vin) {
        List<Client> clients = clientsService.getClients(userId);
        Car car = new Car();
       return clients.stream()

                .filter(client -> client.getId().equals(clientId))
                .findFirst()
                .map(client -> {
                    setClientForCar(clientId,car);
                    modelMapper.map(vinDecoderController.getDecodeVin(vin),car);

                    carRepository.save(car);
                    return true;
                })
                .orElseThrow(() -> new ClientNotFoundException("Client not found"));


    }



    public boolean deleteCarWithClientIdByCarId(Long userId, Long carId) {

        return Optional.ofNullable(checkIfCarIdExist(carId))
                .filter(car -> clientsService.getClients(userId).stream()
                        .anyMatch(client -> client.getId().equals(car.getClient().getId())))
                .map(car -> {
                    carRepository.deleteById(carId);
                    return true;
                })
                .orElseThrow(() -> new CarNotFoundException("Car not found"));

    }

    public Car checkIfCarIdExist(Long carId) {
        Optional<Car> car = carRepository.findById(carId);
        if (car.isPresent())
            return car.get();
        throw new CarNotFoundException("Car with id " + carId + "notExist");
    }

    public boolean hasUserAccessToCar(Long userId, Long carId) {
        return Optional.ofNullable(checkIfCarIdExist(carId))
                .map(car -> clientsService.getClients(userId).stream()
                        .anyMatch(client -> client.getId().equals(car.getClient().getId())))
                .orElse(false);
    }

    private void setClientForCar(Long clientId, Car car) {
        car.setClient(clientsService.checkIfClientIdExist(clientId));
    }


}
