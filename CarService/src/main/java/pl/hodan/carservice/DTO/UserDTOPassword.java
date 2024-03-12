package pl.hodan.carservice.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import pl.hodan.carservice.common.messages.Messages;

@Getter
@Setter
public class UserDTOPassword extends UserDTO {
    @NotBlank(message = Messages.EMPTY_FIELD)
    private String password;


}
