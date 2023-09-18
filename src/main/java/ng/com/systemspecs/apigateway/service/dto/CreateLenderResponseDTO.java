package ng.com.systemspecs.apigateway.service.dto;

import ng.com.systemspecs.apigateway.domain.User;

public class CreateLenderResponseDTO {

    private Long lenderId;
    private String fullName;
    private String phoneNumber;
    private String accountNumber;
    private String accountName;
    private Double rate;
    private String preferredTenure;
    private String token;
    private User user;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Long getLenderId() {
        return lenderId;
    }

    public void setLenderId(Long lenderId) {
        this.lenderId = lenderId;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPreferredTenure() {
        return preferredTenure;
    }

    public void setPreferredTenure(String preferredTenure) {
        this.preferredTenure = preferredTenure;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "CreateLenderResponseDTO{" +
            "lenderId=" + lenderId +
            ", fullName='" + fullName + '\'' +
            ", phoneNumber='" + phoneNumber + '\'' +
            ", accountNumber='" + accountNumber + '\'' +
            ", accountName='" + accountName + '\'' +
            ", rate=" + rate +
            ", preferredTenure='" + preferredTenure + '\'' +
            ", token='" + token + '\'' +
            ", user=" + user +
            '}';
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
