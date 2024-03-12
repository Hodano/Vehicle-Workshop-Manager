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
import pl.hodan.carservice.common.entity.History;
import pl.hodan.carservice.common.entity.User;
import pl.hodan.carservice.common.service.HistoriesService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HistoryControllerTest {
    @Mock
    private HistoriesService historiesService;
    @Mock
    private AuthenticationService authenticationService;
    @InjectMocks
    private HistoryController historyController;
    private User user;
    @BeforeEach
    public void initializeUserId(){
        user = new User();
        user.setId(1L);
    }
    @Test
    void getHistoriesByCarIdShouldReturnHistories() {
        // given
        Long userId = user.getId();
        Long carId = 2L;
        History history = new History();
        List<History> expectedHistories = List.of(history);

        when(authenticationService.getCurrentUserId()).thenReturn(userId);
        when(historiesService.getHistoriesByCarId(userId, carId)).thenReturn(expectedHistories);
        // when
        ResponseEntity<List<History>> response = historyController.getHistoriesByCarId(carId);
        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedHistories, response.getBody());
    }
    @Test
    void getHistoryByHistoryIdShouldReturnHistory() {
        // given
        Long userId = user.getId();
        Long historyId = 2L;
        History expectedHistory = new History();

        when(authenticationService.getCurrentUserId()).thenReturn(userId);
        when(historiesService.getHistoryByHistoryId(userId, historyId)).thenReturn(expectedHistory);
        // when
        ResponseEntity<History> response = historyController.getHistoryByHistoryId(historyId);
        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedHistory, response.getBody());
    }
    @Test
    void addHistoryShouldReturnCreatedWhenSuccessful() {
        // given
        Long userId = user.getId();
        Long carId = 2L;
        History newHistory = new History();

        when(authenticationService.getCurrentUserId()).thenReturn(userId);
        when(historiesService.addHistory(userId, carId, newHistory)).thenReturn(true);
        // when
        ResponseEntity<String> response = historyController.addHistory(carId, newHistory);
        // then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }
    @Test
    void addHistoryShouldReturnBadRequestWhenUnsuccessful() {
        // given
        Long userId = user.getId();
        Long carId = 2L;
        History newHistory = new History();

        when(authenticationService.getCurrentUserId()).thenReturn(userId);
        when(historiesService.addHistory(userId, carId, newHistory)).thenReturn(false);
        // when
        ResponseEntity<String> response = historyController.addHistory(carId, newHistory);
        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
    @Test
    void modifyHistoryShouldReturnOkWhenSuccessful() {
        // given
        Long userId = user.getId();
        Long historyId = 3L;
        History updatedHistory = new History();

        when(authenticationService.getCurrentUserId()).thenReturn(userId);
        when(historiesService.modifyHistoryWithCarIdByHistoryId(userId, historyId, updatedHistory)).thenReturn(true);
        // when
        ResponseEntity<String> response = historyController.modifyHistory(historyId, updatedHistory);
        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    @Test
    void modifyHistoryShouldReturnNotFoundWhenUnsuccessful() {
        // given
        Long userId = user.getId();
        Long historyId = 3L;
        History updatedHistory = new History();

        when(authenticationService.getCurrentUserId()).thenReturn(userId);
        when(historiesService.modifyHistoryWithCarIdByHistoryId(userId, historyId, updatedHistory)).thenReturn(false);
        // when
        ResponseEntity<String> response = historyController.modifyHistory(historyId, updatedHistory);
        // then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
    @Test
    void deleteHistoryShouldReturnOkWhenSuccessful() {
        // given
        Long userId = user.getId();
        Long historyId = 3L;

        when(authenticationService.getCurrentUserId()).thenReturn(userId);
        when(historiesService.deleteHistoryByHistoryId(userId, historyId)).thenReturn(true);
        // when
        ResponseEntity<String> response = historyController.deleteHistory(historyId);
        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    @Test
    void deleteHistory_ShouldReturnNotFoundWhenUnsuccessful() {
        // given
        Long userId = user.getId();
        Long historyId = 3L;

        when(authenticationService.getCurrentUserId()).thenReturn(userId);
        when(historiesService.deleteHistoryByHistoryId(userId, historyId)).thenReturn(false);
        // when
        ResponseEntity<String> response = historyController.deleteHistory(historyId);
        // then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }


}