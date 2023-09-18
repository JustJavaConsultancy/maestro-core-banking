package ng.com.systemspecs.apigateway.service.dto.stp;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class WalletREsponseDTO {

    @JsonProperty("accountNumber")
    private String accountNumber;
    @JsonProperty("customerId")
    private String customerId;
    @JsonProperty("accountName")
    private String accountName;
    @JsonProperty("currency")
    private String currency;
    @JsonProperty("accountOpeningDate")
    private String accountOpeningDate;
    @JsonProperty("availableBalance")
    private BigDecimal availableBalance;
    @JsonProperty("lastTransactionDate")
    private String lastTransactionDate;
    @JsonProperty("accountType")
    private String accountType;
    @JsonProperty("bvn")
    private String bvn;
    @JsonProperty("fullName")
    private String fullName;
    @JsonProperty("phoneNumber")
    private String phoneNumber;
    @JsonProperty("email")
    private String email;
    @JsonProperty("status")
    private String status;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getAccountOpeningDate() {
        return accountOpeningDate;
    }

    public void setAccountOpeningDate(String accountOpeningDate) {
        this.accountOpeningDate = accountOpeningDate;
    }

    public String getLastTransactionDate() {
        return lastTransactionDate;
    }

    public void setLastTransactionDate(String lastTransactionDate) {
        this.lastTransactionDate = lastTransactionDate;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getBvn() {
        return bvn;
    }

    public void setBvn(String bvn) {
        this.bvn = bvn;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(BigDecimal availableBalance) {
        this.availableBalance = availableBalance;
    }

    @Override
    public String toString() {
        return "WalletREsponseDTO{" +
            "accountNumber='" + accountNumber + '\'' +
            ", customerId='" + customerId + '\'' +
            ", accountName='" + accountName + '\'' +
            ", currency='" + currency + '\'' +
            ", accountOpeningDate='" + accountOpeningDate + '\'' +
            ", availableBalance='" + availableBalance + '\'' +
            ", lastTransactionDate='" + lastTransactionDate + '\'' +
            ", accountType='" + accountType + '\'' +
            ", bvn='" + bvn + '\'' +
            ", fullName='" + fullName + '\'' +
            ", phoneNumber='" + phoneNumber + '\'' +
            ", email='" + email + '\'' +
            ", status='" + status + '\'' +
            '}';
    }
}
