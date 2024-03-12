package pl.hodan.carservice.common.service;

import org.springframework.stereotype.Service;
import pl.hodan.carservice.auth.services.UsersService;
import pl.hodan.carservice.common.entity.PriceList;
import pl.hodan.carservice.common.exception.PriceListNotFoundException;
import pl.hodan.carservice.common.repository.PriceListRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PriceListService {
    private final PriceListRepository priceListRepository;
    private final UsersService usersService;

    public PriceListService(PriceListRepository priceListRepository, UsersService usersService) {
        this.priceListRepository = priceListRepository;
        this.usersService = usersService;
    }

    public List<PriceList> getPriceListsByUserId(Long userId) {
        usersService.getUserIfUserExist(userId);

        return priceListRepository.findPriceListByUserId(userId);
    }

    public PriceList getPriceListById(Long userId, Long calendarId) {
        usersService.getUserIfUserExist(userId);

        Optional<PriceList> priceList = priceListRepository.findPriceListByUserIdAndId(userId, calendarId);

        return priceList
                .orElseThrow(() -> new PriceListNotFoundException("PriceList with this id: " + calendarId + "not found"));
    }

    public boolean addPriceList(Long userId, PriceList priceList) {
        if (priceList != null) {
            setUserForPriceList(userId, priceList);
            priceListRepository.save(priceList);
            return true;
        }
        return false;

    }

    public boolean modifyPriceListWithUserIdByPriceListId(Long userId, Long priceListId, PriceList newPriceList) {

        checkIfPriceListIdExistByUserId(userId, priceListId);

        Optional<PriceList> priceList = priceListRepository.findPriceListByUserIdAndId(userId, priceListId);
        if (priceList.isPresent()) {
            priceList.get().setNameOfService(newPriceList.getNameOfService());
            priceList.get().setPrices(newPriceList.getPrices());
            priceListRepository.save(priceList.get());
            return true;
        }

        return false;
    }

    public boolean deletePriceListWithUserIdByPriceListId(Long userId, Long priceListId) {
        checkIfPriceListIdExistByUserId(userId, priceListId);

        Optional<PriceList> priceList = priceListRepository.findPriceListByUserIdAndId(userId, priceListId);
        if (priceList.isPresent()) {
            priceListRepository.deleteById(priceListId);
            return true;
        }
        return false;
    }

    public void setUserForPriceList(Long userId, PriceList priceList) {
        priceList.setUser(usersService.getUserIfUserExist(userId));
    }

    public void checkIfPriceListIdExistByUserId(Long userId, Long priceListId) {
        usersService.getUserIfUserExist(userId);
        if (!priceListRepository.existsPriceListById(priceListId))
            throw new PriceListNotFoundException("PriceList with id " + priceListId + "notExist");
    }
}
