package pl.hodan.carservice.auth.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.hodan.carservice.auth.dto.RegisterRequest;
import pl.hodan.carservice.common.entity.UserBasicInformation;

@RequiredArgsConstructor
@Service
public class UserBasicInformationService {
    public UserBasicInformation createUserBasicInformation(RegisterRequest request) {
        UserBasicInformation userBasicInformation = new UserBasicInformation();

        userBasicInformation.setName(request.getName());
        userBasicInformation.setSurname(request.getSurname());
        userBasicInformation.setAddress(request.getAddress());
        userBasicInformation.setPhoneNumber(request.getPhoneNumber());

        return userBasicInformation;
    }

}
