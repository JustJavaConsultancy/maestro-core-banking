package ng.com.systemspecs.apigateway.service.dto.stp;

import com.fasterxml.jackson.annotation.JsonProperty;
import ng.com.systemspecs.apigateway.util.ValidPassword;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public class AccountDto {

    private String requestId;

    @NotEmpty(message = "firstname cannot be empty")
    private String firstName;

    @NotEmpty(message = "firstname cannot be empty")
    private String surname;

    private String otherName;

    @Email
    @NotEmpty
    private String email;

    @Past
    @NotNull
    private LocalDate dateOfBirth;

//    @Size(min = 11, max = 11, message = "Bvn must be 11 numbers")
    private String bvn;

    @NotEmpty
    @Size(min = 11, max = 15, message = "Invalid phone number")
    private String phone;

    private String street;

    private String country;

    private String nationality;

    private String customerId;

    private String accountNumber;

    private String accountName;

    @NotEmpty(message = "Gender cannot be empty")
    private String gender;

    private String transactionPin;

    @ValidPassword(message = "Invalid password, please input a strong password")
    private String secretKey;

    @JsonProperty("schemeCode")
    private String bankCode;

    private String accountTypeId;

    private Boolean isRestrictedWallet = Boolean.FALSE;

    private String restrictedGroupId;

    private String partnerId;

    private BigDecimal overdraftAmount;

    public BigDecimal getOverdraftAmount() {
        return overdraftAmount;
    }

    public void setOverdraftAmount(BigDecimal overdraftAmount) {
        this.overdraftAmount = overdraftAmount;
    }

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

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getFirstName() {
        return firstName;
    }


    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }


    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
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

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getBvn() {
        return bvn;
    }


    public void setBvn(String bvn) {
        this.bvn = bvn;
    }


    public String getPhone() {
        return phone;
    }


    public void setPhone(String phone) {
        this.phone = phone;
    }


    public String getStreet() {
        return street;
    }


    public void setStreet(String street) {
        this.street = street;
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


    public String getAccountNumber() {
        return accountNumber;
    }


    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }


    public String getGender() {
        return gender;
    }


    public void setGender(String gender) {
        this.gender = gender;
    }


    public String getTransactionPin() {
        return transactionPin;
    }


    public void setTransactionPin(String transactionPin) {
        this.transactionPin = transactionPin;
    }


    public String getSecretKey() {
        return secretKey;
    }


    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public Boolean getRestrictedWallet() {
        return isRestrictedWallet;
    }

    public void setRestrictedWallet(Boolean restrictedWallet) {
        isRestrictedWallet = restrictedWallet;
    }

    public String getRestrictedGroupId() {
        return restrictedGroupId;
    }

    public void setRestrictedGroupId(String restrictedGroupId) {
        this.restrictedGroupId = restrictedGroupId;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

}
