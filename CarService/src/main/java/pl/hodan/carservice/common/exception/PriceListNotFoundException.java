package pl.hodan.carservice.common.exception;

public class PriceListNotFoundException extends RuntimeException{
    public PriceListNotFoundException( String message) {
        super(message);
    }
}
