package pl.hodan.carservice.common.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.hodan.carservice.DTO.UserDTO;
import pl.hodan.carservice.DTO.UserDTOPassword;
import pl.hodan.carservice.auth.services.UsersService;

import java.util.List;
import java.util.Optional;

@RequestMapping("/cars-service")
@RestController
public class UserController {
    private final UsersService usersService;

    public UserController(UsersService usersService) {
        this.usersService = usersService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public List<UserDTO> getUsers() {
        return usersService.getUsers();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user")
    public ResponseEntity<UserDTO> getUserById(@RequestParam Long userId) {
        Optional<UserDTO> userDTOOptional = usersService.getUserById(userId);

        return userDTOOptional.map(userDTO -> new ResponseEntity<>(userDTO, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("add-user")
    public ResponseEntity<String> createUser(@Valid @RequestBody UserDTOPassword userDTOPassword) {
        if (usersService.createUser(userDTOPassword))
            return new ResponseEntity<>(HttpStatus.CREATED);
        return new ResponseEntity<>("User could not be added", HttpStatus.BAD_REQUEST);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("add-admin")
    public ResponseEntity<String> createAdmin(@Valid @RequestBody UserDTOPassword userDTOPassword) {
        if (usersService.createAdmin(userDTOPassword))
            return new ResponseEntity<>(HttpStatus.CREATED);
        return new ResponseEntity<>("Admin could not be added", HttpStatus.BAD_REQUEST);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("modify-user")
    public ResponseEntity<String> modifyUser(@RequestParam Long userId, @Valid @RequestBody UserDTOPassword userDTOPassword) {
        if (usersService.modifyUser(userId, userDTOPassword))
            return new ResponseEntity<>("User modified", HttpStatus.ACCEPTED);
        return new ResponseEntity<>("User not found or could not be modified", HttpStatus.NOT_FOUND);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("delete-user")
    public ResponseEntity<String> removeUser(@RequestParam Long id) {
        if (usersService.removeUserById(id))
            return new ResponseEntity<>("User deleted", HttpStatus.ACCEPTED);
        return new ResponseEntity<>("User not found or could not be deleted", HttpStatus.BAD_REQUEST);
    }


}
