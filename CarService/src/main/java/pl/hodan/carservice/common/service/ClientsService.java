package pl.hodan.carservice.common.service;

import org.springframework.stereotype.Service;
import pl.hodan.carservice.auth.services.UsersService;
import pl.hodan.carservice.common.entity.Client;
import pl.hodan.carservice.common.exception.ClientNotFoundException;
import pl.hodan.carservice.common.repository.ClientRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ClientsService {
    private final ClientRepository clientRepository;

    private final UsersService usersService;

    public ClientsService(ClientRepository clientRepository, UsersService usersService) {
        this.clientRepository = clientRepository;
        this.usersService = usersService;
    }

    public List<Client> getClients(Long userId) {
        usersService.getUserIfUserExist(userId);

        return clientRepository.findClientByUserId(userId);
    }

    public Client getClientByUserIdAndClientId(Long userId, Long clientId) {
        usersService.getUserIfUserExist(userId);

        Optional<Client> client = clientRepository.findClientByUserIdAndId(userId, clientId);

        return client
                .orElseThrow(() -> new ClientNotFoundException("Client with this id: " + clientId + "not found"));
    }

    public boolean addClient(Long userId, Client client) {
        if (client != null) {
            setUserForClient(userId, client);
            clientRepository.save(client);
            return true;
        }
        return false;

    }

    public boolean modifyClientWithUserIdByClientId(Long userId, Long clientId, Client newClient) {

        checkIfClientExistByUserId(userId, clientId);

        Optional<Client> client = clientRepository.findClientByUserIdAndId(userId, clientId);
        if (client.isPresent()) {
            client.get().setName(newClient.getName());
            client.get().setSurname(newClient.getSurname());
            client.get().setAddress(newClient.getAddress());
            client.get().setPhoneNumber(newClient.getPhoneNumber());
            client.get().setEmail(newClient.getEmail());
            clientRepository.save(client.get());
            return true;
        }

        return false;
    }

    public boolean deleteClientWithUserIdByClientId(Long userId, Long clientId) {
        checkIfClientExistByUserId(userId, clientId);

        Optional<Client> client = clientRepository.findClientByUserIdAndId(userId, clientId);
        if (client.isPresent()) {
            clientRepository.deleteById(clientId);
            return true;
        }
        return false;
    }

    public List<Client> searchClients(Long userId, String searchTerm) {
        return clientRepository.findBySearchTermAndUserId(searchTerm, userId);
    }

    public Client checkIfClientIdExist(Long clientId) {
        Optional<Client> client = clientRepository.findById(clientId);
        if (client.isPresent())
            return client.get();
        throw new ClientNotFoundException("Client with id " + clientId + "notExist");
    }


    public void setUserForClient(Long userId, Client client) {
        client.setUser(usersService.getUserIfUserExist(userId));
    }

    private void checkIfClientExistByUserId(Long userId, Long clientId) {
        usersService.getUserIfUserExist(userId);
        if (!clientRepository.existsClientById(clientId))
            throw new ClientNotFoundException("Client with id " + clientId + "notExist");

    }


}
