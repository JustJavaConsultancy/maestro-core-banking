package ng.com.justjava.corebanking.service.dto.stp;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

public class AccountValidationDTO {

    @NotEmpty(message = "Account Number cannot be null")
    private String accountNumber;

    @NotEmpty(message = "First name cannot be null")
    private String firstname;

    @NotEmpty(message = "Last name cannot be null")
    private String lastname;

    @NotEmpty(message = "Phone Number cannot be null")
    private String phoneNumber;

    @Past(message = "Birth date must be in the past")
    @NotNull(message = "Date of birth cannot be null")
    private String dateBirth;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDateBirth() {
        return dateBirth;
    }

    public void setDateBirth(String dateBirth) {
        this.dateBirth = dateBirth;
    }

    @Override
    public String toString() {
        return "AccountValidationDTO{" +
            "accountNumber='" + accountNumber + '\'' +
            ", firstname='" + firstname + '\'' +
            ", lastname='" + lastname + '\'' +
            ", phoneNumber='" + phoneNumber + '\'' +
            ", dateBirth=" + dateBirth +
            '}';
    }
}
