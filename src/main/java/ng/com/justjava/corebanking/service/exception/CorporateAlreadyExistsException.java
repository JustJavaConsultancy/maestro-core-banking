package ng.com.justjava.corebanking.service.exception;

public class CorporateAlreadyExistsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public CorporateAlreadyExistsException() {
        super("Corporate Profile with Phone Number or RC No already exists!");
    }

}
