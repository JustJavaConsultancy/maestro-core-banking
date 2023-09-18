package ng.com.justjava.corebanking.service.dto.stp;


import javax.validation.constraints.NotEmpty;
import java.io.Serializable;


@SuppressWarnings("serial")
public class NameEnquiryRequest implements Serializable {

    @NotEmpty(message = "Account number cannot be null")
    private String accountNumber;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    @Override
    public String toString() {
        return "NameEnquiryRequest{" +
            "accountNumber='" + accountNumber + '\'' +
            '}';
    }
}
