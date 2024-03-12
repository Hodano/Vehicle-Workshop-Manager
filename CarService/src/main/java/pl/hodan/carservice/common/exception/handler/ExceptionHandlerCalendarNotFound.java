package pl.hodan.carservice.common.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.hodan.carservice.common.exception.CalendarNotFoundException;

@ControllerAdvice
public class ExceptionHandlerCalendarNotFound {
    @ExceptionHandler(CalendarNotFoundException.class)
    public ResponseEntity<String> handleCalendarNotFound(CalendarNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Calendar not found");
    }
}
