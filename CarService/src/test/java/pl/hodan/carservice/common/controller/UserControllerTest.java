package pl.hodan.carservice.common.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.hodan.carservice.DTO.UserDTO;
import pl.hodan.carservice.DTO.UserDTOPassword;
import pl.hodan.carservice.auth.services.UsersService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @Mock
    private UsersService usersService;
    @InjectMocks
    private UserController userController;


    @Test
    public void getsUsersShouldReturnUserList() {
        //given
        UserDTO userDTO1 = new UserDTO();
        userDTO1.setName("Mariusz");
        UserDTO userDTO2 = new UserDTO();
        userDTO2.setName("Tomasz");

        List<UserDTO> userDTOList = Arrays.asList(userDTO1, userDTO2);
        when(usersService.getUsers()).thenReturn(userDTOList);
        //when
        List<UserDTO> actual = userController.getUsers();
        //then
        assertNotNull(actual);
        assertEquals(userDTOList, actual);
        assertEquals(2, actual.size());


    }

    @Test
    public void getUserByIdShouldReturnUserWhenUserExist() {
        //given
        Long userId = 1L;
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userId);

        when(usersService.getUserById(userId)).thenReturn(Optional.of(userDTO));
        //when
        ResponseEntity<UserDTO> response = userController.getUserById(userId);
        //then
        assertNotNull(response.getBody());
        assertEquals(userDTO, response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(usersService).getUserById(userId);

    }

    @Test
    public void getUserShouldReturnNotFoundWhenUserDoesNotExist() {
        //given
        Long userId = 1L;
        when(usersService.getUserById(userId)).thenReturn(Optional.empty());
        //when
        ResponseEntity<UserDTO> response = userController.getUserById(userId);
        //then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    }

    @Test
    public void createUserShouldReturnHttpStatusCreatedWhenSuccessful() {
        //given
        UserDTOPassword userDTOPassword = new UserDTOPassword();
        when(usersService.createUser(userDTOPassword)).thenReturn(true);
        //when
        ResponseEntity<String> response = userController.createUser(userDTOPassword);
        //then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void createUserShouldReturnStatusBadRequestWhenUnsuccessful() {
        //given
        UserDTOPassword userDTOPassword = new UserDTOPassword();
        when(usersService.createUser(userDTOPassword)).thenReturn(false);
        //when
        ResponseEntity<String> response = userController.createUser(userDTOPassword);
        //then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(usersService).createUser(any(UserDTOPassword.class));
    }

    @Test
    public void modifyUserShouldReturnStatusAcceptedWhenUserSuccessful() {
        //given
        Long userId = 1L;
        UserDTOPassword userDTOPassword = new UserDTOPassword();


        when(usersService.modifyUser(userId, userDTOPassword)).thenReturn(true);
        //when
        ResponseEntity<String> response = userController.modifyUser(userId, userDTOPassword);
        //then
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        verify(usersService).modifyUser(anyLong(), any(UserDTOPassword.class));
    }

    @Test
    public void modifyUserShouldReturnStatusAcceptedWhenUnsuccessful() {
        //given
        Long userId = 1L;
        UserDTOPassword userDTOPassword = new UserDTOPassword();


        when(usersService.modifyUser(userId, userDTOPassword)).thenReturn(false);
        //when
        ResponseEntity<String> response = userController.modifyUser(userId, userDTOPassword);
        //then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(usersService).modifyUser(anyLong(), any(UserDTOPassword.class));
    }

    @Test
    public void RemoveShouldReturnStatusAcceptedWhenUserSuccessful() {
        //given
        Long userId = 1L;
        when(usersService.removeUserById(userId)).thenReturn(true);
        //when
        ResponseEntity<String> response = userController.removeUser(userId);
        //then
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        verify(usersService).removeUserById(anyLong());

    }

    @Test
    public void RemoveShouldReturnStatusBadRequestWhenUnsuccessful() {
        //given
        Long userId = 1L;
        when(usersService.removeUserById(userId)).thenReturn(false);
        //when
        ResponseEntity<String> response = userController.removeUser(userId);
        //then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(usersService).removeUserById(anyLong());

    }


}