package pl.hodan.carservice.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.hodan.carservice.common.entity.PriceList;

import java.util.List;
import java.util.Optional;

@Repository
public interface PriceListRepository extends JpaRepository<PriceList, Long> {
    List<PriceList> findPriceListByUserId(Long userId);
    Optional<PriceList> findPriceListByUserIdAndId(Long userId, Long calendarId);
    Boolean existsPriceListById(Long id);
}
