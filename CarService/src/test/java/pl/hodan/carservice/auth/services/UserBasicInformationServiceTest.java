package pl.hodan.carservice.auth.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import pl.hodan.carservice.auth.dto.RegisterRequest;
import pl.hodan.carservice.common.entity.UserBasicInformation;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class UserBasicInformationServiceTest {
    @Test
    public void createUserBasicInformationShouldCorrectlyMapFields() {
        //given
        UserBasicInformationService userBasicInformationService = new UserBasicInformationService();
        RegisterRequest request = new RegisterRequest("Mariusz4@onet.pl", "12345", "Mariusz", "Kowalczyk", "Gdow 321", 323432123);
        //when
        UserBasicInformation userBasicInformation = userBasicInformationService.createUserBasicInformation(request);
        //then
        assertEquals(request.getName(),userBasicInformation.getName());
        assertEquals(request.getSurname(),userBasicInformation.getSurname());
        assertEquals(request.getAddress(),userBasicInformation.getAddress());
        assertEquals(request.getPhoneNumber(),userBasicInformation.getPhoneNumber());

    }

}