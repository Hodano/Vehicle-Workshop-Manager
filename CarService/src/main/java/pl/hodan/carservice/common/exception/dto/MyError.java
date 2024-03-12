package pl.hodan.carservice.common.exception.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MyError {
    private String field;
    private String message;



    public MyError(String field, String message) {
        this.field = field;
        this.message = message;
    }

    public static MyError of(String field, String message) {
        return new MyError(field, message);
    }
}
