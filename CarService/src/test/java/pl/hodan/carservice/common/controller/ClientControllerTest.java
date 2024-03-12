package pl.hodan.carservice.common.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.hodan.carservice.DTO.SearchClientDTO;
import pl.hodan.carservice.auth.services.AuthenticationService;
import pl.hodan.carservice.common.entity.Client;
import pl.hodan.carservice.common.entity.User;
import pl.hodan.carservice.common.service.ClientsService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientControllerTest {
    @Mock
    ClientsService clientsService;
    @Mock
    AuthenticationService authenticationService;
    @InjectMocks
    ClientController clientController;
    private User user;
    @BeforeEach
    public void initializeUserId(){
        user = new User();
        user.setId(1L);
    }
    @Test
    void getClientsByUserIdShouldReturnClients() {
        //given
        Long userId = user.getId();
        Client client = new Client();
        List<Client> expectedClients = List.of(client);

        when(authenticationService.getCurrentUserId()).thenReturn(userId);
        when(clientsService.getClients(userId)).thenReturn(expectedClients);
        //when
        ResponseEntity<List<Client>> response = clientController.getClientsByUserId();
        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    @Test
    void getClientByClientIdShouldReturnClient() {
        // given
        Long userId = user.getId();
        Long clientId = 2L;
        Client expectedClient = new Client();

        when(authenticationService.getCurrentUserId()).thenReturn(userId);
        when(clientsService.getClientByUserIdAndClientId(userId, clientId)).thenReturn(expectedClient);
        // when
        ResponseEntity<Client> response = clientController.getClientByClientId(clientId);
        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    @Test
    void addClientShouldReturnCreatedWhenSuccessful() {
        // given
        Long userId = user.getId();
        Client newClient = new Client();

        when(authenticationService.getCurrentUserId()).thenReturn(userId);
        when(clientsService.addClient(userId, newClient)).thenReturn(true);
        // when
        ResponseEntity<String> response = clientController.addClient(newClient);
        // then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }
    @Test
    void addClientShouldReturnBadRequestWhenUnsuccessful() {
        // given
        Long userId = user.getId();
        Client newClient = new Client();

        when(authenticationService.getCurrentUserId()).thenReturn(userId);
        when(clientsService.addClient(userId, newClient)).thenReturn(false);
        // when
        ResponseEntity<String> response = clientController.addClient(newClient);
        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
    @Test
    void searchClientsSShouldReturnFilteredClients() {
        // given
        Long userId = user.getId();
        String searchTerm = "test";
        List<Client> expectedClients = List.of(new Client());
        when(authenticationService.getCurrentUserId()).thenReturn(userId);
        when(clientsService.searchClients(userId, searchTerm)).thenReturn(expectedClients);

        SearchClientDTO searchClientDTO = new SearchClientDTO();
        searchClientDTO.setSearchClient(searchTerm);
        // when
        ResponseEntity<List<Client>> response = clientController.searchClients(searchClientDTO);
        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedClients, response.getBody());
    }
    @Test
    void modifyClientShouldReturnOkWhenSuccessful() {
        // given
        Long userId = user.getId();
        Long clientId = 2L;
        Client client = new Client();
        when(authenticationService.getCurrentUserId()).thenReturn(userId);
        when(clientsService.modifyClientWithUserIdByClientId(userId, clientId, client)).thenReturn(true);
        // when
        ResponseEntity<String> response = clientController.modifyClient(clientId, client);
        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    @Test
    void modifyClientShouldReturnNotFoundWhenUnsuccessful() {
        // given
        Long userId = user.getId();
        Long clientId = 2L;
        Client client = new Client();
        when(authenticationService.getCurrentUserId()).thenReturn(userId);
        when(clientsService.modifyClientWithUserIdByClientId(userId, clientId, client)).thenReturn(false);
        // when
        ResponseEntity<String> response = clientController.modifyClient(clientId, client);
        // then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
    @Test
    void deleteClientShouldReturnOkWhenSuccessful() {
        // given
        Long userId = user.getId();
        Long clientId = 2L;
        when(authenticationService.getCurrentUserId()).thenReturn(userId);
        when(clientsService.deleteClientWithUserIdByClientId(userId, clientId)).thenReturn(true);
        // when
        ResponseEntity<String> response = clientController.deleteClient(clientId);
        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    @Test
    void deleteClientShouldReturnNotFoundWhenUnsuccessful() {
        //given
        Long userId = user.getId();
        Long clientId = 2L;
        when(authenticationService.getCurrentUserId()).thenReturn(userId);
        when(clientsService.deleteClientWithUserIdByClientId(userId, clientId)).thenReturn(false);
        //when
        ResponseEntity<String> response = clientController.deleteClient(clientId);

        //then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }


}