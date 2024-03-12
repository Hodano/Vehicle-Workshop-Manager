package pl.hodan.carservice.common.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import pl.hodan.carservice.auth.services.UsersService;
import pl.hodan.carservice.common.entity.Client;
import pl.hodan.carservice.common.entity.User;
import pl.hodan.carservice.common.entity.UserBasicInformation;
import pl.hodan.carservice.common.exception.ClientNotFoundException;
import pl.hodan.carservice.common.exception.UserNotFoundException;
import pl.hodan.carservice.common.repository.ClientRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientsServiceTest {
    private User user;
    private Client client;
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private UsersService usersService;
    @InjectMocks
    private ClientsService clientsService;

    @BeforeEach
    public void createAndInitialize() {
        client = new Client();
        client.setId(1L);
        client.setName("Stefan");
        client.setSurname("Nowak");
        client.setEmail("mariusz22@onet.pl");
        client.setAddress("Sułkowice 304");
        client.setPhoneNumber(532123221);

        UserBasicInformation userBasicInformation = new UserBasicInformation(1L, "Stefan", "Nowak", "Sułkowice 304", 532123221);
        user = new User(1L, "mariusz22@onet.pl", "1234", userBasicInformation);
    }

    @Test
    public void getClientsShouldReturnClientsWhenExist() {
        //given
        List<Client> clientList = Arrays.asList(client);
        Long userId = user.getId();

        when(usersService.getUserIfUserExist(userId)).thenReturn(user);
        when(clientRepository.findClientByUserId(userId)).thenReturn(clientList);

        //when
        List<Client> result = clientsService.getClients(userId);
        //then
        assertEquals(clientList, result);
        assertEquals(clientList.size(), result.size());
        assertNotNull(result);
        verify(clientRepository).findClientByUserId(anyLong());


    }

    @Test
    public void getClientsShouldReturnEmptyListWhenClientListIsEmpty() {
        //given
        List<Client> emptyList = new ArrayList<>();
        Long userId = user.getId();

        when(usersService.getUserIfUserExist(userId)).thenReturn(user);
        when(clientRepository.findClientByUserId(userId)).thenReturn(emptyList);

        //when
        List<Client> result = clientsService.getClients(userId);
        //then
        assertTrue(result.isEmpty());
        assertNotNull(result);
        verify(clientRepository).findClientByUserId(anyLong());
    }

    @Test
    public void getClientByClientIdReturnClientWhenExist() {
        //given
        Long userId = user.getId();
        Long clientId = client.getId();

        when(usersService.getUserIfUserExist(userId)).thenReturn(user);
        when(clientRepository.findClientByUserIdAndId(userId, clientId)).thenReturn(Optional.of(client));
        //when
        Client result = clientsService.getClientByUserIdAndClientId(userId, clientId);
        //then
        assertNotNull(result);
        assertEquals(client, result);
        verify(usersService).getUserIfUserExist(anyLong());
        verify(clientRepository).findClientByUserIdAndId(anyLong(), anyLong());

    }

    @Test
    public void getClientShouldThrowClientNotFoundExceptionWhenClientDoesExist() {
        //given
        Long userId = user.getId();
        Long clientId = client.getId();

        when(usersService.getUserIfUserExist(userId)).thenReturn(user);
        when(clientRepository.findClientByUserIdAndId(userId, clientId)).thenReturn(Optional.empty());
        //when
        //then
        assertThrows(ClientNotFoundException.class, () -> clientsService.getClientByUserIdAndClientId(userId, clientId));
    }

    @Test
    public void getClientShouldThrowUSerNotFoundExceptionWhenUserIsNotExist() {
        //given
        Long userId = user.getId();
        Long clientId = client.getId();

        //when
        when(usersService.getUserIfUserExist(userId)).thenThrow(new UserNotFoundException("User not found"));

        //then
        assertThrows(UserNotFoundException.class, () -> clientsService.getClientByUserIdAndClientId(userId, clientId));
    }

    @Test
    public void addClientShouldReturnTrueWhenClientExist() {
        //given
        Long userId = user.getId();

        when(usersService.getUserIfUserExist(userId)).thenReturn(user);
        //when
        boolean result = clientsService.addClient(userId, client);
        //then
        assertTrue(result);
        verify(clientRepository).save(any(Client.class));

    }

    @Test
    public void addClientShouldReturnFalseWhenClientIsNotExist() {
        //given
        Long userId = user.getId();

        //when
        boolean result = clientsService.addClient(userId, null);
        //then
        assertFalse(result);
    }

    @Test
    public void modifyClientShouldReturnTrueWhenClientExist() {
        //given
        Long userId = user.getId();
        Long clientId = client.getId();
        Client newClient = new Client();

        when(usersService.getUserIfUserExist(userId)).thenReturn(user);
        when(clientRepository.existsClientById(clientId)).thenReturn(true);

        when(clientRepository.findClientByUserIdAndId(userId, clientId)).thenReturn(Optional.of(client));
        //when
        boolean result = clientsService.modifyClientWithUserIdByClientId(userId, clientId, newClient);
        //then
        assertTrue(result);
        verify(clientRepository).save(client);
        verify(usersService).getUserIfUserExist(anyLong());
        verify(clientRepository).findClientByUserIdAndId(anyLong(), anyLong());
    }

    @Test
    public void modifyClientShouldReturnFalseWhenClientIsNotExist() {
        //given
        Long userId = user.getId();
        Long clientId = client.getId();
        Client newClient = new Client();

        when(usersService.getUserIfUserExist(userId)).thenReturn(user);
        when(clientRepository.existsClientById(clientId)).thenReturn(true);

        when(clientRepository.findClientByUserIdAndId(userId, clientId)).thenReturn(Optional.empty());
        //when
        boolean result = clientsService.modifyClientWithUserIdByClientId(userId, clientId, newClient);
        //then
        assertFalse(result);
        verify(usersService).getUserIfUserExist(anyLong());
        verify(clientRepository).findClientByUserIdAndId(anyLong(), anyLong());
    }

    @Test
    public void deleteClientShouldReturnTrueWhenClientExist() {
        //given
        Long userId = user.getId();
        Long clientId = client.getId();

        when(usersService.getUserIfUserExist(userId)).thenReturn(user);
        when(clientRepository.existsClientById(clientId)).thenReturn(true);

        when(clientRepository.findClientByUserIdAndId(userId, clientId)).thenReturn(Optional.of(client));

        //when
        boolean result = clientsService.deleteClientWithUserIdByClientId(userId, clientId);
        //then
        assertTrue(result);
        verify(clientRepository).deleteById(clientId);
        verify(usersService).getUserIfUserExist(anyLong());

    }

    @Test
    public void deleteClientShouldReturnFalseWhenCalendarIsNotExist() {
        //given
        Long userId = user.getId();
        Long clientId = client.getId();

        when(usersService.getUserIfUserExist(userId)).thenReturn(user);
        when(clientRepository.existsClientById(clientId)).thenReturn(true);

        when(clientRepository.findClientByUserIdAndId(userId, clientId)).thenReturn(Optional.empty());
        //when
        boolean result = clientsService.deleteClientWithUserIdByClientId(userId, clientId);
        //then
        assertFalse(result);
        verify(usersService).getUserIfUserExist(anyLong());
        verify(clientRepository).existsClientById(anyLong());
    }
    @Test
    public void ShouldSetUserForClient(){
        //given
        Long userId = user.getId();
        when(usersService.getUserIfUserExist(userId)).thenReturn(user);
        //when
        clientsService.setUserForClient(userId,client);
        //then
        assertEquals(user,client.getUser());
        verify(usersService).getUserIfUserExist(anyLong());
    }


}