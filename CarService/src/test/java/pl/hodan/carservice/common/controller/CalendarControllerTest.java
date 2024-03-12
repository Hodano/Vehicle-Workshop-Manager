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
import pl.hodan.carservice.common.entity.Calendar;
import pl.hodan.carservice.common.entity.User;
import pl.hodan.carservice.common.exception.CalendarNotFoundException;
import pl.hodan.carservice.common.exception.UserNotFoundException;
import pl.hodan.carservice.common.service.CalendarsService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CalendarControllerTest {
    @Mock
    private CalendarsService calendarsService;
    @Mock
    private AuthenticationService authenticationService;
    @InjectMocks
    private CalendarController calendarController;
    private User user;
    @BeforeEach
    public void initializeUserId(){
        user = new User();
        user.setId(1L);
    }

    @Test
    public void getCalendarsByUserIdShouldReturnCalendarsList() {
        // given
        Long userId = user.getId();
        Calendar calendar = new Calendar();
        calendar.setEvent("event");


        List<Calendar> expectedCalendars = List.of(calendar);
        when(authenticationService.getCurrentUserId()).thenReturn(userId);
        when(calendarsService.getCalendarsByUserId(userId)).thenReturn(expectedCalendars);
        // when
        ResponseEntity<List<Calendar>> response = calendarController.getCalendarsByUserId();
        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedCalendars, response.getBody());
    }

    @Test
    public void getCalendarsByUserIdShouldThrowUserNotFoundExceptionWhenUserDoesNotExist() {
        //given
        Long userId = user.getId();
        when(authenticationService.getCurrentUserId()).thenReturn(userId);
        when(calendarsService.getCalendarsByUserId(userId)).thenThrow(UserNotFoundException.class);
        //when
        //then
        assertThrows(UserNotFoundException.class, () -> calendarController.getCalendarsByUserId());
    }

    @Test
    public void getCalendarByCalendarIdShouldReturnCalendarWhenUserAndCalendarExist() {
        // given
        Long userId = user.getId();
        Long calendarId = 2L;
        Calendar calendar = new Calendar();
        when(authenticationService.getCurrentUserId()).thenReturn(userId);
        when(calendarsService.getCalendarByUserIdAndCalendarId(userId, calendarId)).thenReturn(calendar);
        // when
        ResponseEntity<Calendar> response = calendarController.getCalendarByCalendarId(calendarId);
        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(calendar, response.getBody());
    }

    @Test
    public void getCalendarShouldCalendarNotFoundExceptionWhenCalendarDoesNotExist() {
        // given
        Long userId = user.getId();
        Long calendarId = 2L;
        when(authenticationService.getCurrentUserId()).thenReturn(userId);
        when(calendarsService.getCalendarByUserIdAndCalendarId(userId, calendarId)).thenThrow(CalendarNotFoundException.class);

        // when
        // then
        assertThrows(CalendarNotFoundException.class, () -> calendarController.getCalendarByCalendarId(calendarId));
    }
    @Test
    void addCalendarShouldReturnCreatedStatusWhenSuccessful() {
        // given
        Long userId = user.getId();
        Calendar calendar = new Calendar();
        when(authenticationService.getCurrentUserId()).thenReturn(userId);
        when(calendarsService.addCalendar(userId, calendar)).thenReturn(true);
        // when
        ResponseEntity<String> response = calendarController.addCalendar(calendar);
        // then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }
    @Test
    void addCalendarShouldReturnBadRequestWhenUnsuccessful() {
        // given
        Long userId = user.getId();
        Calendar calendar = new Calendar();
        when(authenticationService.getCurrentUserId()).thenReturn(userId);
        when(calendarsService.addCalendar(userId, calendar)).thenReturn(false);
        // when
        ResponseEntity<String> response = calendarController.addCalendar(calendar);
        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(calendarsService).addCalendar(anyLong(),any(Calendar.class));
    }
    @Test
    void modifyCalendarShouldReturnAcceptedWhenSuccessful() {
        // given
        Long userId = user.getId();
        Long calendarId = 2L;
        Calendar newCalendar = new Calendar();
        when(authenticationService.getCurrentUserId()).thenReturn(userId);
        when(calendarsService.modifyCalendarWithUserIdByCalendarId(userId, calendarId, newCalendar)).thenReturn(true);
        // when
        ResponseEntity<String> response = calendarController.modifyCalendar(calendarId, newCalendar);
        // then
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
    }
    @Test
    void modifyCalendarShouldReturnNotFoundWhenUnsuccessful() {
        // given
        Long userId = user.getId();
        Long calendarId = 2L;
        Calendar newCalendar = new Calendar();
        when(authenticationService.getCurrentUserId()).thenReturn(userId);
        when(calendarsService.modifyCalendarWithUserIdByCalendarId(userId, calendarId, newCalendar)).thenReturn(false);
        // when
        ResponseEntity<String> response = calendarController.modifyCalendar(calendarId, newCalendar);
        // then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        verify(calendarsService).modifyCalendarWithUserIdByCalendarId(anyLong(), anyLong(), any(Calendar.class)); // Weryfikacja, czy próbowano zmodyfikować kalendarz
    }
    @Test
    void deleteCalendarShouldReturnOkWhenSuccessful() {
        // given
        Long userId = user.getId();
        Long calendarId = 2L;
        when(authenticationService.getCurrentUserId()).thenReturn(userId);
        when(calendarsService.deleteCalendarWithUserIdByCalendarId(userId, calendarId)).thenReturn(true);
        // when
        ResponseEntity<String> response = calendarController.deleteCalendar(calendarId);
        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(calendarsService).deleteCalendarWithUserIdByCalendarId(anyLong(),anyLong());
    }
    @Test
    void deleteCalendarShouldReturnNotFoundWhenUnsuccessful() {
        // given
        Long userId = user.getId();
        Long calendarId = 2L;
        when(authenticationService.getCurrentUserId()).thenReturn(userId);
        when(calendarsService.deleteCalendarWithUserIdByCalendarId(userId, calendarId)).thenReturn(false);
        // when
        ResponseEntity<String> response = calendarController.deleteCalendar(calendarId);
        // then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(calendarsService).deleteCalendarWithUserIdByCalendarId(userId, calendarId);
    }


}