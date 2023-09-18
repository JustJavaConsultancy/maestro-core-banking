package ng.com.justjava.corebanking.service.dto.stp;


import javax.validation.constraints.NotEmpty;
import java.io.Serializable;


@SuppressWarnings("serial")
public class AuthenticateOTPRequest implements Serializable {

    @NotEmpty(message = "Account number cannot be empty")
    private String accountNumber;

    @NotEmpty(message = "Mobile number cannot be empty")
    private String mobileNumber;

    @NotEmpty(message = "Otp cannot be empty")
    private String otp;

    @NotEmpty(message = "Pin cannot be empty")
    private String pin;

    @NotEmpty(message = "Date of birth cannot be empty")
    private String dob;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    @Override
    public String toString() {
        return "AuthenticateOTPRequest{" +
            "accountNumber='" + accountNumber + '\'' +
            ", mobileNumber='" + mobileNumber + '\'' +
            ", otp='" + otp + '\'' +
            ", pin='" + pin + '\'' +
            ", dob='" + dob + '\'' +
            '}';
    }
}
