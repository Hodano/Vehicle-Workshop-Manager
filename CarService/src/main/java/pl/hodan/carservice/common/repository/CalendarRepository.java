package pl.hodan.carservice.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.hodan.carservice.common.entity.Calendar;

import java.util.List;
import java.util.Optional;

@Repository
public interface CalendarRepository extends JpaRepository<Calendar, Long> {
    List<Calendar> findCalendarsByUserId(Long userId);

    Optional<Calendar> findCalendarByUserIdAndId(Long userId, Long calendarId);

    Boolean existsCalendarById(Long calendarId);


}
