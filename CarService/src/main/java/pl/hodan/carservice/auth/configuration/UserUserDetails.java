package pl.hodan.carservice.auth.configuration;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pl.hodan.carservice.common.entity.Role;
import pl.hodan.carservice.common.entity.User;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@EqualsAndHashCode
public class UserUserDetails implements UserDetails {

    private final String email;
    private final String password;

    @Getter
    private final Long id;
    @Getter
    private final String name;
    @Getter
    private final String surname;
    @Getter
    private final String address;
    @Getter
    private final int phoneNumber;
    @Getter

    private final List<SimpleGrantedAuthority> authorities;

    public UserUserDetails(User user) {
        email = user.getEmail();
        password = user.getPassword();
        id = user.getId();
        name = user.getUserBasicInformation().getName();
        surname = user.getUserBasicInformation().getSurname();
        address = user.getUserBasicInformation().getAddress();
        phoneNumber = user.getUserBasicInformation().getPhoneNumber();
        authorities = user.getRoles()
                .stream()
                .map(Role::getRole)
                .map(roleName -> new SimpleGrantedAuthority("ROLE_" + roleName.name()))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
