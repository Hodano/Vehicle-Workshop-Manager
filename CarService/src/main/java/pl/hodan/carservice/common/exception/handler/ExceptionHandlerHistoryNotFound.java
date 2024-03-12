package pl.hodan.carservice.common.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.hodan.carservice.common.exception.HistoryNotFoundException;

@ControllerAdvice
public class ExceptionHandlerHistoryNotFound {
    @ExceptionHandler(HistoryNotFoundException.class)
    public ResponseEntity<String> handleHistoryNotFound(HistoryNotFoundException exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("History not found");
    }
}
