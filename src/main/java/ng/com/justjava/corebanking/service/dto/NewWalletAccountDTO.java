package ng.com.justjava.corebanking.service.dto;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;

public class NewWalletAccountDTO implements Serializable {

    @NotEmpty
    private String requestId;

    @NotEmpty
    private String accountTypeId;

    @NotEmpty
    private String firstName;

    private String otherName;

    @NotEmpty
    private String surname;

    @NotEmpty
    private String gender;

    private String dateOfBirth;

    @Size(min = 11)
    private String bvn;

    @NotEmpty
    private String street;

    private String city;

    private String localGovernment;

    private String country;

    private String nationality;

    @NotEmpty
    private String customerId;

    @Size(min = 11, max = 15, message = "phone number must be greater than 10 and less than 15 characters")
    private String phone;

    @Email(message = "Email has invalid format", regexp = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$")
    private String email;

    @NotEmpty
    private String secretKey;

    @NotEmpty
    private String transactionPin;

    @NotEmpty
    private String schemeCode;

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

    public String getOtherName() {
        return otherName;
    }

    public void setOtherName(String otherName) {
        this.otherName = otherName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getBvn() {
        return bvn;
    }

    public void setBvn(String bvn) {
        this.bvn = bvn;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLocalGovernment() {
        return localGovernment;
    }

    public void setLocalGovernment(String localGovernment) {
        this.localGovernment = localGovernment;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getTransactionPin() {
        return transactionPin;
    }

    public void setTransactionPin(String transactionPin) {
        this.transactionPin = transactionPin;
    }

    public String getSchemeCode() {
        return schemeCode;
    }

    public void setSchemeCode(String schemeCode) {
        this.schemeCode = schemeCode;
    }

    @Override
    public String toString() {
        return "NewWalletAccountDTO{" +
            "requestId='" + requestId + '\'' +
            ", accountTypeId='" + accountTypeId + '\'' +
            ", firstName='" + firstName + '\'' +
            ", otherName='" + otherName + '\'' +
            ", surname='" + surname + '\'' +
            ", gender='" + gender + '\'' +
            ", dateOfBirth='" + dateOfBirth + '\'' +
            ", bvn='" + bvn + '\'' +
            ", street='" + street + '\'' +
            ", city='" + city + '\'' +
            ", localGovernment='" + localGovernment + '\'' +
            ", country='" + country + '\'' +
            ", nationality='" + nationality + '\'' +
            ", customerId='" + customerId + '\'' +
            ", phone='" + phone + '\'' +
            ", email='" + email + '\'' +
            ", secretKey='" + secretKey + '\'' +
            ", transactionPin='" + transactionPin + '\'' +
            ", schemeCode='" + schemeCode + '\'' +
            '}';
    }
}
