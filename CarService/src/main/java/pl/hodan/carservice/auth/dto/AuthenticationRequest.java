package pl.hodan.carservice.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.hodan.carservice.common.messages.Messages;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {
    @NotBlank(message = Messages.EMPTY_FIELD)
    @Email(message = Messages.BAD_EMAIL)
    private String email;
    @NotBlank(message = Messages.EMPTY_FIELD)
    private String password;
}
