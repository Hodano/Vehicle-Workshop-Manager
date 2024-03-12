package pl.hodan.carservice.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import pl.hodan.carservice.common.messages.Messages;

import java.util.Set;

@Data
@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = Messages.EMPTY_FIELD)
    private String name;
    @NotBlank(message = Messages.EMPTY_FIELD)
    private String surname;
    @NotBlank(message = Messages.EMPTY_FIELD)
    private String address;
    @NotNull(message = Messages.PHONE_NUMBER_IS_NULL)
    private int phoneNumber;
    @Email(message = Messages.BAD_EMAIL)
    @NotBlank
    private String email;
    @JsonIgnore
    @ManyToOne
    private User user;
    @JsonIgnore
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private Set<Car> carSet;


    public Client() {
    }
}
