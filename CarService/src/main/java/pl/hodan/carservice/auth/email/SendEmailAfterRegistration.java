package pl.hodan.carservice.auth.email;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
public @interface SendEmailAfterRegistration {
}
