package pl.hodan.carservice.common.exception.handler;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import pl.hodan.carservice.common.exception.ValidationException;
import pl.hodan.carservice.common.exception.dto.MyError;
import pl.hodan.carservice.common.exception.dto.ValidationErrorList;

import java.util.stream.Collectors;

@ControllerAdvice
public class ErrorValidationHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorList> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        ValidationErrorList validationErrorList = ValidationErrorList.of(e.getBindingResult()
                .getAllErrors()
                .stream()
                .map(this::mapToMyError)
                .collect(Collectors.toSet()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validationErrorList);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ValidationErrorList> handleConstraintViolationException(ConstraintViolationException e) {
        ValidationErrorList validationErrorList = ValidationErrorList.of(e.getConstraintViolations()
                .stream()
                .map(constraintViolation -> new MyError(
                        constraintViolation.getPropertyPath().toString(),
                        constraintViolation.getMessage()))
                .collect(Collectors.toSet())
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validationErrorList);
    }


    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ValidationErrorList> validationException(ValidationException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getValidationErrorList());
    }

    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    public ResponseEntity<MyError> AuthenticationCredentialsNotFoundException(AuthenticationCredentialsNotFoundException e) {
        MyError myError = MyError.of("Authentication", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(myError);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<MyError> accessDeniedException(AccessDeniedException e) {
        MyError myError = MyError.of("Access", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(myError);
    }

    //vin
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ValidationErrorList> IllegalArgumentException(IllegalArgumentException e) {
        ValidationErrorList validationErrorList = ValidationErrorList.of("Illegal_argument_error", e.getMessage());
        return validationErrorList.createResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpClientErrorException.Unauthorized.class)
    public ResponseEntity<ValidationErrorList> HttpClientErrorExceptionUnauthorized(HttpClientErrorException e) {
        ValidationErrorList validationErrorList = ValidationErrorList.of("httpClientApi", e.getMessage());
        return validationErrorList.createResponseEntity(HttpStatus.UNAUTHORIZED);
    }


    public MyError mapToMyError(ObjectError error) {
        FieldError fieldError = (FieldError) error;
        return MyError.of(fieldError.getField(), fieldError.getDefaultMessage());
    }
}
