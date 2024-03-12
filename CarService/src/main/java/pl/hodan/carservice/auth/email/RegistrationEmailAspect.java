package pl.hodan.carservice.auth.email;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import pl.hodan.carservice.DTO.UserDTOPassword;
import pl.hodan.carservice.auth.dto.RegisterRequest;
import pl.hodan.carservice.auth.email.service.EmailService;

@Aspect
@Component
public class RegistrationEmailAspect {
    private final EmailService emailService;
    Logger logger = LoggerFactory.getLogger(RegistrationEmailAspect.class);

    public RegistrationEmailAspect(EmailService emailService) {
        this.emailService = emailService;
    }

    @AfterReturning("@annotation(pl.hodan.carservice.auth.email.SendEmailAfterRegistration)")
    public void sendEmailAfterRegistration(JoinPoint joinpoint) {
        Object[] args = joinpoint.getArgs();
        if (args.length > 0 && args[0] instanceof RegisterRequest) {
            String email = ((RegisterRequest) args[0]).getEmail();

            emailService.sendEmail(email, "CarService", "Thank you for registering");
        } else
            logger.error("email does not exist");

    }

    @AfterReturning("@annotation(pl.hodan.carservice.auth.email.SendEmailByUserCreatedByAdmin)")
    public void sendEmailAfterCreatedByAdmin(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        if (args.length > 0 && args[0] instanceof UserDTOPassword) {
            String email = ((UserDTOPassword) args[0]).getEmail();

            emailService.sendEmail(email, "CarService", "Thank you for registering");
        } else
            logger.error("email does not exist");

    }

}
