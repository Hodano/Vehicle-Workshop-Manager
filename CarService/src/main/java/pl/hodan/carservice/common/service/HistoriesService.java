package pl.hodan.carservice.common.service;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import pl.hodan.carservice.common.entity.History;
import pl.hodan.carservice.common.exception.CarNotFoundException;
import pl.hodan.carservice.common.exception.HistoryNotFoundException;
import pl.hodan.carservice.common.repository.HistoryRepository;


import java.util.List;
import java.util.Optional;

@Service
public class HistoriesService {
    private final HistoryRepository historyRepository;
    private final CarsService carsService;

    public HistoriesService(HistoryRepository historyRepository, CarsService carsService) {
        this.historyRepository = historyRepository;
        this.carsService = carsService;
    }

    public List<History> getHistoriesByCarId(Long userId, Long carId) {
        carsService.checkIfCarIdExist(carId);

        if (!carsService.hasUserAccessToCar(userId, carId))
            throw new AccessDeniedException("User does not have access to this car");

        return historyRepository.findHistoriesByCarId(carId);
    }

    public History getHistoryByHistoryId(Long userId, Long historyId) {
        History history = checkIfHistoryIdExist(historyId);
        Long carId = history.getCar().getId();

        carsService.checkIfCarIdExist(carId);

        if (!carsService.hasUserAccessToCar(userId, carId))
            throw new AccessDeniedException("User does not have access to this car");

        return history;
    }

    public boolean addHistory(Long userId, Long carId, History history) {

        setCarForHistory(carId, history);

        if (!carsService.hasUserAccessToCar(userId, carId))
            throw new AccessDeniedException("User does not have access to this car");

        historyRepository.save(history);
        return true;
    }

    public boolean modifyHistoryWithCarIdByHistoryId(Long userId, Long historyId, History newHistory) {
        History history = checkIfHistoryIdExist(historyId);
        Long carId = history.getCar().getId();

        carsService.checkIfCarIdExist(carId);

        if (!carsService.hasUserAccessToCar(userId, carId))
            throw new AccessDeniedException("User does not have access to this car");


        history.setDateOfHistoryCar(newHistory.getDateOfHistoryCar());
        history.setDescriptionHistory(newHistory.getDescriptionHistory());

        historyRepository.save(history);
        return true;

    }

    public boolean deleteHistoryByHistoryId(Long userId, Long historyId) {

        History history = checkIfHistoryIdExist(historyId);
        Long carId = history.getCar().getId();

        carsService.checkIfCarIdExist(carId);

        if (!carsService.hasUserAccessToCar(userId, carId))
            throw new AccessDeniedException("User does not have access to this car");

            historyRepository.deleteById(historyId);
            return true;

    }

    private void setCarForHistory(Long carId, History history) {
        history.setCar(carsService.checkIfCarIdExist(carId));
    }


    public History checkIfHistoryIdExist(Long historyId) {
        Optional<History> history = historyRepository.findById(historyId);
        if (history.isPresent()) {
            return history.get();
        }
        throw new HistoryNotFoundException("History with this id " + historyId + "not exist");
    }
}
