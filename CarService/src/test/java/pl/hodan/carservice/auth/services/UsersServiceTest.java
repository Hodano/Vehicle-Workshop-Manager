package pl.hodan.carservice.auth.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.hodan.carservice.DTO.UserDTO;
import pl.hodan.carservice.DTO.UserDTOPassword;
import pl.hodan.carservice.common.entity.Role;
import pl.hodan.carservice.common.entity.User;
import pl.hodan.carservice.common.entity.UserBasicInformation;
import pl.hodan.carservice.common.enums.RolesEnum;
import pl.hodan.carservice.common.exception.UserNotFoundException;
import pl.hodan.carservice.common.exception.ValidationException;
import pl.hodan.carservice.common.repository.UserRepository;
import pl.hodan.carservice.common.service.RoleService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsersServiceTest {
    private UserBasicInformation userBasicInformation;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleService roleService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    ModelMapper modelMapper;
    @InjectMocks
    private UsersService usersService;
    private User user;

    @BeforeEach
    public void initializeUser() {


        userBasicInformation = new UserBasicInformation(1L, "Stefan", "Nowak", "Sułkowice 304", 532123221);
        user = new User(1L, "mariusz22@onet.pl", "1234", userBasicInformation);
    }

    public UserDTOPassword createUserDTOPassword() {
        UserDTOPassword userDTOPassword = new UserDTOPassword();
        userDTOPassword.setName(userBasicInformation.getName());
        userDTOPassword.setSurname(userBasicInformation.getSurname());
        userDTOPassword.setAddress(userBasicInformation.getAddress());
        userDTOPassword.setPhoneNumber(userBasicInformation.getPhoneNumber());
        userDTOPassword.setId(1L);
        userDTOPassword.setEmail("mariususz22@onet.pl");
        userDTOPassword.setPassword("12345");
        return userDTOPassword;
    }

    public UserDTO createUserDTO() {
        UserDTO userDTO = new UserDTO();
        userDTO.setName(userBasicInformation.getName());
        userDTO.setSurname(userBasicInformation.getSurname());
        userDTO.setAddress(userBasicInformation.getAddress());
        userDTO.setPhoneNumber(userBasicInformation.getPhoneNumber());
        userDTO.setId(1L);
        userDTO.setEmail("mariususz22@onet.pl");

        return userDTO;
    }


    @Test
    public void shouldReturnUserWhenUserWithIdExist() {
        //given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        //when
        User exist = usersService.getUserIfUserExist(1L);

        //then
        assertNotNull(exist);
        assertEquals(user, exist);
    }

    @Test
    public void shouldThrowAnExceptionWhenUserIsNotExist() {
        //given
        Long userId = user.getId();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        //when
        //then
        assertThrows(UserNotFoundException.class, () -> usersService.getUserIfUserExist(userId));
    }

    @Test
    public void shouldThrowExceptionWhenEmailExist() {
        //given
        String email = "mariusz400@onet.pl";
        when(userRepository.existsByEmail(email)).thenReturn(true);
        //when
        //then
        assertThrows(ValidationException.class, () -> usersService.checkIsEmailAlreadyExist(email));
    }

    @Test
    public void shouldReturnFalseWhenEmailIsNotExist() {
        //given
        String email = "emailNoExist";
        //when
        when(userRepository.existsByEmail(email)).thenReturn(false);
        boolean actual = usersService.checkIsEmailAlreadyExist(email);
        //then
        assertFalse(actual);
    }

    @Test
    public void ShouldReturnUsersWhenExist() {
        //given
        List<User> userList = Arrays.asList(user);

        when(userRepository.findAll()).thenReturn(userList);
        when(modelMapper.map(any(User.class), eq(UserDTO.class))).thenReturn(new UserDTO());
        //when
        List<UserDTO> actual = usersService.getUsers();
        //then
        assertNotNull(actual);
        assertEquals(userList.size(), actual.size());

    }

    @Test
    public void ShouldBeReturnListOfUserDTOs() {
        //given
        List<User> userList = Arrays.asList(user);
        UserDTO userDTO = createUserDTO();

        //when
        when(userRepository.findAll()).thenReturn(userList);
        when(modelMapper.map(any(User.class), eq(UserDTO.class))).thenReturn(userDTO);
        UserDTO actual = usersService.getUsers().get(0);
        //then

        assertEquals(userDTO.getEmail(), actual.getEmail());
        assertEquals(userDTO.getName(), actual.getName());
        verify(modelMapper).map(any(User.class), eq(UserDTO.class));


    }

    @Test
    public void createUserShouldReturnTrue() {
        // given
        UserDTOPassword userDTOPassword = createUserDTOPassword();

        User mappedUser = new User();
        when(modelMapper.map(userDTOPassword, User.class)).thenReturn(mappedUser);
        when(passwordEncoder.encode(userDTOPassword.getPassword())).thenReturn("encodedPassword");
        when(roleService.setUserRole()).thenReturn(new Role(1L, RolesEnum.USER));

        // when
        boolean result = usersService.createUser(userDTOPassword);

        // then
        assertTrue(result);
        verify(userRepository).save(any(User.class));
        verify(modelMapper).map(any(UserDTOPassword.class), eq(User.class));
    }

    @Test
    public void modifyUserShouldReturnTrueWhenWeSearchedExistUser() {
        //given
        Long userId = 1L;
        UserDTOPassword userDTOPassword = createUserDTOPassword();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(userDTOPassword.getPassword())).thenReturn("encodedPassword");
        //when

        boolean result = usersService.modifyUser(userId, userDTOPassword);

        //then
        assertTrue(result);
        verify(userRepository).save(any(User.class));


    }

    @Test
    public void modifyUserShouldReturnFalseWhenWeNotSearchedExistUser() {
        //given
        Long userId = user.getId();
        UserDTOPassword userDTOPassword = createUserDTOPassword();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        //when

        boolean result = usersService.modifyUser(userId, userDTOPassword);

        //then
        assertFalse(result);


    }

    @Test
    public void removeUserShouldReturnTrueWhenUserExists() {
        //given
        Long userId = user.getId();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        //when
        boolean result = usersService.removeUserById(userId);

        //then
        assertTrue(result);
        verify(userRepository).delete(user);
    }

    @Test
    public void removeUserShouldReturnFalseWhenUserDoesNotExist() {
        //given
        Long userId = user.getId();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        //when
        boolean result = usersService.removeUserById(userId);

        //then
        assertFalse(result);
        verify(userRepository, never()).delete(user);
    }
}
