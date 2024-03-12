package pl.hodan.carservice.common.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import pl.hodan.carservice.auth.services.UsersService;
import pl.hodan.carservice.common.entity.PriceList;
import pl.hodan.carservice.common.entity.User;
import pl.hodan.carservice.common.entity.UserBasicInformation;
import pl.hodan.carservice.common.exception.PriceListNotFoundException;
import pl.hodan.carservice.common.exception.UserNotFoundException;
import pl.hodan.carservice.common.repository.PriceListRepository;

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
class PriceListServiceTest {
    private PriceList priceList;
    private User user;
    @Mock
    private PriceListRepository priceListRepository;
    @Mock
    private UsersService usersService;
    @InjectMocks
    private PriceListService priceListService;

    @BeforeEach
    public void createAndInitialize() {
        priceList = new PriceList();
        priceList.setId(1L);
        priceList.setPrices(3.42);
        priceList.setNameOfService("replacement of brake pads");

        UserBasicInformation userBasicInformation = new UserBasicInformation(1L, "Stefan", "Nowak", "Sułkowice 304", 532123221);
        user = new User(1L, "mariusz22@onet.pl", "1234", userBasicInformation);
    }


    @Test
    public void getPricesListShouldReturnPriceListsWhenExist() {
        // given
        List<PriceList> priceListList = Arrays.asList(priceList);
        Long userId = user.getId();

        when(usersService.getUserIfUserExist(userId)).thenReturn(user);
        when(priceListRepository.findPriceListByUserId(userId)).thenReturn(priceListList);

        // when
        List<PriceList> resultList = priceListService.getPriceListsByUserId(userId);

        // then
        assertNotNull(resultList);
        assertEquals(priceListList.size(), resultList.size());
        assertEquals(priceListList, resultList);
        verify(priceListRepository).findPriceListByUserId(anyLong());
    }


    @Test
    public void getPricesListShouldReturnEmptyListWhenPriceListIsEmpty() {
        // given
        List<PriceList> emptyList = new ArrayList<>();
        Long userId = user.getId();

        when(usersService.getUserIfUserExist(userId)).thenReturn(user);
        when(priceListRepository.findPriceListByUserId(userId)).thenReturn(emptyList);

        // when
        List<PriceList> resultList = priceListService.getPriceListsByUserId(userId);

        // then
        assertTrue(resultList.isEmpty());
        verify(priceListRepository).findPriceListByUserId(anyLong());
    }

    @Test
    public void getPriceListByIdShouldReturnPriceList() {
        // given
        Long userId = user.getId();
        Long priceListId = priceList.getId();

        when(usersService.getUserIfUserExist(userId)).thenReturn(user);
        when(priceListRepository.findPriceListByUserIdAndId(userId, priceListId)).thenReturn(Optional.of(priceList));
        // when
        PriceList result = priceListService.getPriceListById(userId, priceListId);
        // then
        assertNotNull(result);
        assertEquals(priceList, result);
        verify(usersService).getUserIfUserExist(userId);
        verify(priceListRepository).findPriceListByUserIdAndId(anyLong(), anyLong());
    }

    @Test
    public void shouldThrowPriceListNotFoundExceptionWhenPriceListDoesNotExist() {
        // given
        Long userId = user.getId();
        Long priceListId = priceList.getId();

        when(usersService.getUserIfUserExist(userId)).thenReturn(user);
        when(priceListRepository.findPriceListByUserIdAndId(userId, priceListId)).thenReturn(Optional.empty());
        // when
        // then
        assertThrows(PriceListNotFoundException.class, () -> priceListService.getPriceListById(userId, priceListId));
    }

    @Test
    public void shouldThrowUserNotFoundExceptionWhenUserDoesNotExistForPriceList() {
        // given
        Long userId = user.getId();
        Long priceListId = priceList.getId();

        // when
        when(usersService.getUserIfUserExist(userId)).thenThrow(new UserNotFoundException("User not found"));

        // then
        assertThrows(UserNotFoundException.class,
                () -> priceListService.getPriceListById(userId, priceListId));
    }

    @Test
    public void addPriceListShouldReturnTrueWhenPriceListExist() {
        // given
        Long userId = user.getId();
        when(usersService.getUserIfUserExist(userId)).thenReturn(user);
        // when
        boolean result = priceListService.addPriceList(userId, priceList);
        // then
        assertTrue(result);
        verify(priceListRepository).save(any(PriceList.class));
    }

    @Test
    public void addPriceListShouldReturnFalseWhenPriceListIsNull() {
        //given
        Long userId = user.getId();
        //when
        boolean result = priceListService.addPriceList(userId, null);

        //then
        assertFalse(result);
    }

    @Test
    public void modifyPriceListShouldReturnTrueWhenPriceListExist() {
        // given
        PriceList newPriceList = new PriceList();
        Long userId = user.getId();
        Long priceListId = priceList.getId();

        when(usersService.getUserIfUserExist(priceListId)).thenReturn(user);
        when(priceListRepository.existsPriceListById(priceListId)).thenReturn(true);

        when(priceListRepository.findPriceListByUserIdAndId(userId, priceListId)).thenReturn(Optional.of(priceList));
        // when
        boolean result = priceListService.modifyPriceListWithUserIdByPriceListId(userId, priceListId, newPriceList);
        // then
        assertTrue(result);
        verify(priceListRepository).save(priceList);
        verify(usersService).getUserIfUserExist(anyLong());
        verify(priceListRepository).findPriceListByUserIdAndId(anyLong(), anyLong());
    }

    @Test
    public void modifyPriceListShouldReturnFalseWhenPriceListDoesNotExist() {
        // given
        PriceList newPriceList = new PriceList();
        Long userId = user.getId();
        Long priceListId = priceList.getId();

        when(usersService.getUserIfUserExist(priceListId)).thenReturn(user);
        when(priceListRepository.existsPriceListById(priceListId)).thenReturn(true);

        when(priceListRepository.findPriceListByUserIdAndId(userId, priceListId)).thenReturn(Optional.empty());
        // when
        boolean result = priceListService.modifyPriceListWithUserIdByPriceListId(userId, priceListId, newPriceList);
        // then
        assertFalse(result);
        verify(usersService).getUserIfUserExist(anyLong());
        verify(priceListRepository).findPriceListByUserIdAndId(anyLong(), anyLong());
    }

    @Test
    public void deletePriceListShouldReturnTrueWhenPriceListExist() {
        // given
        Long userId = user.getId();
        Long priceListId = priceList.getId();

        when(usersService.getUserIfUserExist(priceListId)).thenReturn(user);
        when(priceListRepository.existsPriceListById(priceListId)).thenReturn(true);

        when(priceListRepository.findPriceListByUserIdAndId(userId, priceListId)).thenReturn(Optional.of(priceList));
        // when
        boolean result = priceListService.deletePriceListWithUserIdByPriceListId(userId, priceList.getId());
        // then
        assertTrue(result);
        verify(priceListRepository).deleteById(priceList.getId());
        verify(usersService).getUserIfUserExist(anyLong());
    }

    @Test
    public void deletePriceListShouldReturnFalseWhenPriceListDoesNotExist() {
        // given
        Long userId = user.getId();
        Long priceListId = priceList.getId();

        when(usersService.getUserIfUserExist(priceListId)).thenReturn(user);
        when(priceListRepository.existsPriceListById(priceListId)).thenReturn(true);

        when(priceListRepository.findPriceListByUserIdAndId(userId, priceListId)).thenReturn(Optional.empty());
        // when
        boolean result = priceListService.deletePriceListWithUserIdByPriceListId(userId, priceList.getId());
        // then
        assertFalse(result);
        verify(usersService).getUserIfUserExist(anyLong());
    }

    @Test
    public void shouldSetUserForPriceList() {
        // given
        Long userId = user.getId();
        when(usersService.getUserIfUserExist(userId)).thenReturn(user);

        // when
        priceListService.setUserForPriceList(userId, priceList);

        // then
        assertEquals(user, priceList.getUser());
        verify(usersService).getUserIfUserExist(anyLong());
    }

    @Test
    void shouldNotThrowExceptionWhenPriceListExists() {
        // given
        Long userId = user.getId();
        Long priceListId = priceList.getId();

        when(usersService.getUserIfUserExist(userId)).thenReturn(user);
        when(priceListRepository.existsPriceListById(priceListId)).thenReturn(true);

        // when
        // then
        assertDoesNotThrow(() -> priceListService.checkIfPriceListIdExistByUserId(userId, priceListId));
    }


}