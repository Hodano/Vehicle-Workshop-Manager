package pl.hodan.carservice.auth.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.hodan.carservice.common.messages.Messages;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @NotBlank(message = Messages.EMPTY_FIELD)
    @Email(message = Messages.BAD_EMAIL)
    private String email;
    @NotBlank(message = Messages.EMPTY_FIELD)
    private String password;
    @NotBlank(message = Messages.EMPTY_FIELD)
    private String name;
    @NotBlank(message = Messages.EMPTY_FIELD)
    private String surname;
    @NotBlank(message = Messages.EMPTY_FIELD)
    private String address;
    @NotNull(message = Messages.PHONE_NUMBER_IS_NULL)
    private Integer phoneNumber;

}
