package pl.hodan.carservice.common.vindecoder.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import pl.hodan.carservice.common.exception.ValidationException;
import pl.hodan.carservice.common.exception.dto.ValidationErrorList;
import pl.hodan.carservice.common.messages.Messages;

@Component
public class UriBuilderForDecodeVin {
    private static final Logger logger = LoggerFactory.getLogger(UriBuilderForDecodeVin.class);
    @Value("${x.Api.url}")
    private String urlDecoderVinApi;
    @Value("${x.Api.key}")
    private String RapidApiKey;


    public String setHttpParam(String vin) {
        String uri = "";
        if (vin.isEmpty())
            throw new ValidationException(ValidationErrorList.of("vin", Messages.EMPTY_FIELD));
        try {

            uri = UriComponentsBuilder.fromHttpUrl(urlDecoderVinApi)
                    .queryParam("vin", vin)
                    .toUriString();
        } catch (IllegalArgumentException e) {
            logger.error("error while building the URI");
        }
        return uri;


    }

    public HttpEntity<String> setHttpHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();


        httpHeaders.set("Authorization", RapidApiKey);


        return new HttpEntity<>(httpHeaders);

    }
}
