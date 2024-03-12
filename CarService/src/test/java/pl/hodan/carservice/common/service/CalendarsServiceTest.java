package pl.hodan.carservice.common.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import pl.hodan.carservice.auth.services.UsersService;
import pl.hodan.carservice.common.entity.Calendar;
import pl.hodan.carservice.common.entity.User;
import pl.hodan.carservice.common.entity.UserBasicInformation;
import pl.hodan.carservice.common.exception.CalendarNotFoundException;
import pl.hodan.carservice.common.exception.UserNotFoundException;
import pl.hodan.carservice.common.repository.CalendarRepository;

import java.sql.Date;
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
class CalendarsServiceTest {
    private Calendar calendar;
    private User user;
    @Mock
    private CalendarRepository calendarRepository;
    @Mock
    private UsersService usersService;
    @InjectMocks
    private CalendarsService calendarsService;

    @BeforeEach
    public void createAndInitialize() {
        calendar = new Calendar();
        calendar.setId(1L);
        calendar.setEvent("replacement of brake pads");
        calendar.setDateEvent(Date.valueOf("2000-12-22"));


        UserBasicInformation userBasicInformation = new UserBasicInformation(1L, "Stefan", "Nowak", "Sułkowice 304", 532123221);
        user = new User(1L, "mariusz22@onet.pl", "1234", userBasicInformation);
    }


    @Test
    public void getCalendarsShouldReturnCalendarsWhenExist() {

        //given
        List<Calendar> calendarList = Arrays.asList(calendar);
        Long userId = user.getId();

        when(usersService.getUserIfUserExist(userId)).thenReturn(user);
        when(calendarRepository.findCalendarsByUserId(userId)).thenReturn(calendarList);
        //when

        List<Calendar> resultList = calendarsService.getCalendarsByUserId(userId);
        //then
        assertNotNull(resultList);
        assertEquals(calendarList.size(), resultList.size());
        assertEquals(calendarList, resultList);
        verify(calendarRepository).findCalendarsByUserId(anyLong());


    }

    @Test
    public void getCalendarsShouldReturnEmptyListWhenCalendarListIsEmpty() {
        //given
        List<Calendar> emptyList = new ArrayList<>();
        Long userId = user.getId();

        when(usersService.getUserIfUserExist(userId)).thenReturn(user);
        when(calendarRepository.findCalendarsByUserId(userId)).thenReturn(emptyList);
        //when
        List<Calendar> resultList = calendarsService.getCalendarsByUserId(userId);
        //then
        assertTrue(resultList.isEmpty());
        verify(calendarRepository).findCalendarsByUserId(anyLong());

    }

    @Test
    public void getCalendarByUserIdAndCalendarIdShouldReturnCalendar() {
        //given
        Long userId = user.getId();
        Long calendarId = calendar.getId();


        when(usersService.getUserIfUserExist(userId)).thenReturn(user);
        when(calendarRepository.findCalendarByUserIdAndId(userId, calendarId)).thenReturn(Optional.of(calendar));
        //when
        Calendar result = calendarsService.getCalendarByUserIdAndCalendarId(userId, calendarId);
        //then
        assertNotNull(result);
        assertEquals(calendar, result);
        verify(usersService).getUserIfUserExist(anyLong());
        verify(calendarRepository).findCalendarByUserIdAndId(anyLong(), anyLong());
    }

    @Test
    public void getCalendarShouldThrowCalendarNotFoundExceptionWhenCalendarWhenCalendarNotExist() {
        //given
        Long userId = user.getId();
        Long calendarId = calendar.getId();

        when(usersService.getUserIfUserExist(userId)).thenReturn(user);
        when(calendarRepository.findCalendarByUserIdAndId(userId, calendarId)).thenReturn(Optional.empty());
        //when
        //then
        assertThrows(CalendarNotFoundException.class, () -> calendarsService.getCalendarByUserIdAndCalendarId(userId, calendarId));

    }

    @Test
    public void getCalendarShouldThrowUserNotFoundExceptionWhenUserDoesExist() {
        //given
        Long userId = user.getId();
        Long calendarId = calendar.getId();
        //when
        when(usersService.getUserIfUserExist(userId)).thenThrow(new UserNotFoundException("User not found"));
        //then
        assertThrows(UserNotFoundException.class, () -> calendarsService.getCalendarByUserIdAndCalendarId(userId, calendarId));
    }

    @Test
    public void addCalendarShouldReturnTrueWhenCalendarExist() {
        //given
        Long userId = user.getId();
        when(usersService.getUserIfUserExist(userId)).thenReturn(user);
        //when
        boolean result = calendarsService.addCalendar(userId, calendar);
        //then
        assertTrue(result);
        verify(calendarRepository).save(any(Calendar.class));
    }


    @Test
    public void addCalendarShouldReturnFalseWhenCalendarIsNull() {
        //given
        Long userId = user.getId();
        //when
        boolean result = calendarsService.addCalendar(userId, null);
        //then
        assertFalse(result);
    }

    @Test
    public void modifyCalendarShouldReturnTrueWhenCalendarExist() {
        //given
        Calendar newCalendar = new Calendar();
        Long userId = user.getId();
        Long calendarId = calendar.getId();

        when(usersService.getUserIfUserExist(userId)).thenReturn(user);
        when(calendarRepository.existsCalendarById(calendarId)).thenReturn(true);

        when(calendarRepository.findCalendarByUserIdAndId(userId, calendarId)).thenReturn(Optional.of(calendar));
        //when
        boolean result = calendarsService.modifyCalendarWithUserIdByCalendarId(userId, calendar.getId(), newCalendar);

        //then
        assertTrue(result);
        verify(calendarRepository).save(calendar);
        verify(usersService).getUserIfUserExist(anyLong());
        verify(calendarRepository).findCalendarByUserIdAndId(anyLong(), anyLong());
    }

    @Test
    public void modifyCalendarShouldReturnFalseWhenCalendarIsNotExist() {
        //given
        Calendar newCalendar = new Calendar();
        Long userId = user.getId();
        Long calendarId = calendar.getId();


        when(usersService.getUserIfUserExist(userId)).thenReturn(user);
        when(calendarRepository.existsCalendarById(calendarId)).thenReturn(true);

        when(calendarRepository.findCalendarByUserIdAndId(userId, calendarId)).thenReturn(Optional.empty());

        //when

        boolean result = calendarsService.modifyCalendarWithUserIdByCalendarId(userId, calendarId, newCalendar);

        //then
        assertFalse(result);
        verify(usersService).getUserIfUserExist(anyLong());
        verify(calendarRepository).findCalendarByUserIdAndId(anyLong(), anyLong());
    }

    @Test
    public void deleteCalendarShouldReturnTrueWhenCalendarExist() {
        //given
        Long userId = user.getId();
        Long calendarId = calendar.getId();

        when(usersService.getUserIfUserExist(userId)).thenReturn(user);
        when(calendarRepository.existsCalendarById(calendarId)).thenReturn(true);

        when(calendarRepository.findCalendarByUserIdAndId(userId, calendarId)).thenReturn(Optional.of(calendar));
        //when
        boolean result = calendarsService.deleteCalendarWithUserIdByCalendarId(userId, calendarId);

        //then
        assertTrue(result);
        verify(calendarRepository).deleteById(calendarId);
        verify(usersService).getUserIfUserExist(anyLong());
    }

    @Test
    public void deleteCalendarShouldReturnFalseWhenCalendarIsNotExist() {
        //given
        Long userId = user.getId();
        Long calendarId = calendar.getId();

        when(usersService.getUserIfUserExist(userId)).thenReturn(user);
        when(calendarRepository.existsCalendarById(calendarId)).thenReturn(true);

        when(calendarRepository.findCalendarByUserIdAndId(userId, calendarId)).thenReturn(Optional.empty());
        //when
        boolean result = calendarsService.deleteCalendarWithUserIdByCalendarId(userId, calendarId);

        //then
        assertFalse(result);
        verify(usersService).getUserIfUserExist(anyLong());
    }

    @Test
    public void shouldSetUserForCalendar() {
        //given
        Long userId = user.getId();
        when(usersService.getUserIfUserExist(userId)).thenReturn(user);
        //when
        calendarsService.setUserForCalendar(userId, calendar);
        //then
        assertEquals(user, calendar.getUser());
        verify(usersService).getUserIfUserExist(anyLong());

    }

    @Test
    void checkIfShouldNotThrowExceptionWhenCalendarExists() {
        // given
        Long userId = user.getId();
        Long calendarId = calendar.getId();

        when(usersService.getUserIfUserExist(userId)).thenReturn(user);
        when(calendarRepository.existsCalendarById(calendarId)).thenReturn(true);

        //then
        //when
        assertDoesNotThrow(() -> calendarsService.checkIfCalendarIdExistByUserId(user.getId(), calendarId),
                "Should not throw exception");
        verify(usersService).getUserIfUserExist(anyLong());
        verify(calendarRepository).existsCalendarById(anyLong());
    }

    @Test
    void shouldThrowCalendarNotFoundExceptionWhenCalendarDoesNotExist() {
        // given
        Long userId = user.getId();
        Long calendarId = calendar.getId();

        when(usersService.getUserIfUserExist(userId)).thenReturn(new User());
        when(calendarRepository.existsCalendarById(calendarId)).thenReturn(false);

        // when
        // then
        assertThrows(CalendarNotFoundException.class,
                () -> calendarsService.checkIfCalendarIdExistByUserId(user.getId(), calendarId));
    }
}





