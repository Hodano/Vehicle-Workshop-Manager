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
import pl.hodan.carservice.common.entity.PriceList;
import pl.hodan.carservice.common.entity.User;
import pl.hodan.carservice.common.service.PriceListService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PriceListControllerTest {
    @Mock
    private PriceListService priceListService;
    @Mock
    private AuthenticationService authenticationService;
    @InjectMocks
    PriceListController priceListController;
    private User user;
    @BeforeEach
    public void initializeUserId(){
        user = new User();
        user.setId(1L);
    }

    @Test
    void getPriceListsByUserIdShouldReturnPriceLists() {
        // given
        Long userId = user.getId();
        PriceList priceList = new PriceList();
        priceList.setPrices(333.2);

        List<PriceList> expectedPriceLists = List.of(priceList);
        when(authenticationService.getCurrentUserId()).thenReturn(userId);
        when(priceListService.getPriceListsByUserId(userId)).thenReturn(expectedPriceLists);
        // when
        ResponseEntity<List<PriceList>> response = priceListController.getPriceListsByUserId();
        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedPriceLists, response.getBody());
    }

    @Test
    void addPriceListShouldReturnCreatedWhenSuccessful() {
        // given
        Long userId = user.getId();
        PriceList newPriceList = new PriceList();
        when(authenticationService.getCurrentUserId()).thenReturn(userId);
        when(priceListService.addPriceList(userId, newPriceList)).thenReturn(true);
        // when
        ResponseEntity<String> response = priceListController.addPriceList(newPriceList);
        // then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void addPriceListShouldReturnBadRequestWhenUnsuccessful() {
        // given
        Long userId = user.getId();
        PriceList newPriceList = new PriceList();
        when(authenticationService.getCurrentUserId()).thenReturn(userId);
        when(priceListService.addPriceList(userId, newPriceList)).thenReturn(false);
        // when
        ResponseEntity<String> response = priceListController.addPriceList(newPriceList);
        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void modifyPriceListShouldReturnAcceptedWhenSuccessful() {
        // given
        Long userId = user.getId();
        Long priceListId = 2L;
        PriceList updatedPriceList = new PriceList();
        when(authenticationService.getCurrentUserId()).thenReturn(userId);
        when(priceListService.modifyPriceListWithUserIdByPriceListId(userId, priceListId, updatedPriceList)).thenReturn(true);
        // when
        ResponseEntity<String> response = priceListController.modifyPriceList(priceListId, updatedPriceList);
        // then
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
    }

    @Test
    void modifyPriceListShouldReturnNotFoundWhenUnsuccessful() {
        // given
        Long userId = user.getId();
        Long priceListId = 2L;
        PriceList updatedPriceList = new PriceList();
        when(authenticationService.getCurrentUserId()).thenReturn(userId);
        when(priceListService.modifyPriceListWithUserIdByPriceListId(userId, priceListId, updatedPriceList)).thenReturn(false);
        // when
        ResponseEntity<String> response = priceListController.modifyPriceList(priceListId, updatedPriceList);
        // then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deletePriceList_ShouldReturnOkWhenSuccessful() {
        // given
        Long userId = user.getId();
        Long priceListId = 2L;
        when(authenticationService.getCurrentUserId()).thenReturn(userId);
        when(priceListService.deletePriceListWithUserIdByPriceListId(userId, priceListId)).thenReturn(true);
        // when
        ResponseEntity<String> response = priceListController.deletePriceList(priceListId);
        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void deletePriceListShouldReturnNotFoundWhenUnsuccessful() {
        // given
        Long userId = user.getId();
        Long priceListId = 2L;
        when(authenticationService.getCurrentUserId()).thenReturn(userId);
        when(priceListService.deletePriceListWithUserIdByPriceListId(userId, priceListId)).thenReturn(false);
        // when
        ResponseEntity<String> response = priceListController.deletePriceList(priceListId);
        // then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}