package pl.hodan.carservice.common.vindecoder.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;
import pl.hodan.carservice.common.exception.ValidationException;
import pl.hodan.carservice.common.exception.dto.ValidationErrorList;
import pl.hodan.carservice.common.messages.Messages;
import pl.hodan.carservice.common.vindecoder.model.VINDecoderModel;
import pl.hodan.carservice.common.vindecoder.utils.UriBuilderForDecodeVin;

@Controller
public class VinDecoderController {
    private final UriBuilderForDecodeVin uriBuilder;

    public VinDecoderController(UriBuilderForDecodeVin uriBuilder) {
        this.uriBuilder = uriBuilder;
    }


    public VINDecoderModel getDecodeVin(String vin) {


        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<VINDecoderModel> response = restTemplate.exchange(
                uriBuilder.setHttpParam(vin),
                HttpMethod.GET,
                uriBuilder.setHttpHeaders(),
                VINDecoderModel.class);

        if (response.getBody().getVIN() == null)
            throw new ValidationException(ValidationErrorList.of("vin", Messages.VIN_NOT_FOUND));

        return response.getBody();
    }
}
