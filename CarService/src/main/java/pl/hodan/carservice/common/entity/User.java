package pl.hodan.carservice.common.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    @OneToOne(cascade = CascadeType.ALL)
    private UserBasicInformation userBasicInformation;

    @OneToMany(mappedBy = "user")
    private Set<Calendar> calendarSet;
    @OneToMany(mappedBy = "user")
    private Set<PriceList> priceListSet;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Client> clientSet;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))

    private Set<Role> roles;

    public User(Long id, String email, String password, UserBasicInformation userBasicInformation) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.userBasicInformation = userBasicInformation;
    }
}
