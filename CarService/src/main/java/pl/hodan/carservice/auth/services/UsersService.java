package pl.hodan.carservice.auth.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.hodan.carservice.DTO.UserDTO;
import pl.hodan.carservice.DTO.UserDTOPassword;
import pl.hodan.carservice.auth.email.SendEmailByUserCreatedByAdmin;
import pl.hodan.carservice.common.entity.Role;
import pl.hodan.carservice.common.entity.User;
import pl.hodan.carservice.common.exception.UserNotFoundException;
import pl.hodan.carservice.common.exception.ValidationException;
import pl.hodan.carservice.common.exception.dto.ValidationErrorList;
import pl.hodan.carservice.common.messages.Messages;
import pl.hodan.carservice.common.repository.UserRepository;
import pl.hodan.carservice.common.service.RoleService;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class
UsersService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;


    private final ModelMapper modelMapper;

    @Autowired
    public UsersService(UserRepository userRepository, RoleService roleService, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }


    public List<UserDTO> getUsers() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
    }

    public Optional<UserDTO> getUserById(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);

        return userOptional.stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .findFirst();
    }

    @SendEmailByUserCreatedByAdmin
    public boolean createUser(UserDTOPassword userDTOPassword) {

        checkIsEmailAlreadyExist(userDTOPassword.getEmail());

        User user = modelMapper.map(userDTOPassword, User.class);
        user.setPassword(passwordEncoder.encode(userDTOPassword.getPassword()));
        user.setRoles(Set.of(roleService.setUserRole()));

        userRepository.save(user);
        return true;
    }

    @SendEmailByUserCreatedByAdmin
    public boolean createAdmin(UserDTOPassword userDTOPassword) {

        checkIsEmailAlreadyExist(userDTOPassword.getEmail());

        User user = modelMapper.map(userDTOPassword, User.class);
        user.setPassword(passwordEncoder.encode(userDTOPassword.getPassword()));
        user.setRoles(Set.of(roleService.setAdminRole()));

        userRepository.save(user);
        return true;
    }

    public boolean modifyUser(Long userId, UserDTOPassword newUserDTOPassword) {

        Optional<User> searchedUser = userRepository.findById(userId);
        if (searchedUser.isPresent()) {
            User existingUser = searchedUser.get();
            Set<Role> originalRoles = existingUser.getRoles();

            modelMapper.map(newUserDTOPassword, existingUser);

            existingUser.setPassword(passwordEncoder.encode(newUserDTOPassword.getPassword()));
            existingUser.setRoles(originalRoles);

            userRepository.save(existingUser);

            return true;
        }
        return false;

    }

    public boolean removeUserById(Long id) {
        Optional<User> searchedUser = userRepository.findById(id);
        if (searchedUser.isPresent()) {
            userRepository.delete(searchedUser.get());
            return true;
        }
        return false;
    }

    public User getUserIfUserExist(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent())
            return user.get();
        throw new UserNotFoundException("User with id " + userId + "notExist");
    }

    public boolean checkIsEmailAlreadyExist(String email) {
        if (userRepository.existsByEmail(email))
            throw new ValidationException(ValidationErrorList.of("email", Messages.EXIST_EMAIL));
        return false;

    }


}
