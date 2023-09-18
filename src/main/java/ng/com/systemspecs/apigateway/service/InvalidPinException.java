package ng.com.systemspecs.apigateway.service;

public class InvalidPinException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InvalidPinException() {
        super("Incorrect pin");
    }

}
