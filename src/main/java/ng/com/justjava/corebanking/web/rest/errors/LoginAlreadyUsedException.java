package ng.com.justjava.corebanking.web.rest.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class LoginAlreadyUsedException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public LoginAlreadyUsedException() {
        super(ErrorConstants.LOGIN_ALREADY_USED_TYPE, "User Already Exist!!", "userManagement", "userexists");
    }
}
