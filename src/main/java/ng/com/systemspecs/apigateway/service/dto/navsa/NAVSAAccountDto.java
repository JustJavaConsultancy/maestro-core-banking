package ng.com.systemspecs.apigateway.service.dto.navsa;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public class NAVSAAccountDto {

    private String requestId;

    @NotEmpty(message = "firstname cannot be empty")
    private String firstName;

    @NotEmpty(message = "firstname cannot be empty")
    private String lastName;

    private String accountName;

    private String otherName;

    @Email
    @NotEmpty
    private String email;

    @NotEmpty
    @Size(min = 11, max = 15, message = "Invalid phone number")
    private String phoneNumber;

    @NotNull
    private String dateOfBirth;

    private String customerId;

    private String accountTypeId;

    @NotEmpty(message = "Gender cannot be empty")
    private String gender;

    private List<OtherAccount> otherAccounts;


    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getAccountTypeId() {
        return accountTypeId;
    }

    public void setAccountTypeId(String accountTypeId) {
        this.accountTypeId = accountTypeId;
    }

    public String getFirstName() {
        return firstName;
    }


    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }


    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getOtherName() {
        return otherName;
    }


    public void setOtherName(String otherName) {
        this.otherName = otherName;
    }


    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }


    public String getPhoneNumber() {
        return phoneNumber;
    }


    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    public String getCustomerId() {
        return customerId;
    }


    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }


    public String getGender() {
        return gender;
    }


    public void setGender(String gender) {
        this.gender = gender;
    }

    public List<OtherAccount> getOtherAccounts() {
        return otherAccounts;
    }

    public void setOtherAccounts(List<OtherAccount> otherAccounts) {
        this.otherAccounts = otherAccounts;
    }

    @Override
    public String toString() {
        return "NAVSAAccountDto{" +
            "requestId='" + requestId + '\'' +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", accountName='" + accountName + '\'' +
            ", otherName='" + otherName + '\'' +
            ", email='" + email + '\'' +
            ", phoneNumber='" + phoneNumber + '\'' +
            ", dateOfBirth=" + dateOfBirth +
            ", customerId='" + customerId + '\'' +
            ", accountTypeId='" + accountTypeId + '\'' +
            ", gender='" + gender + '\'' +
            ", otherAccounts=" + otherAccounts +
            '}';
    }
}
