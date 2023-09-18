package ng.com.justjava.corebanking.service.dto;

import ng.com.justjava.corebanking.domain.User;

public class CreateAgentResponseDTO {

    private Long agentId;
    private String fullName;
    private String phoneNumber;
    private String accountNumber;
    private String accountName;
    private String location;
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

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "CreateAgentResponseDTO{" +
            "id=" + agentId +
            ", fullName='" + fullName + '\'' +
            ", phoneNumber='" + phoneNumber + '\'' +
            ", accountNumber='" + accountNumber + '\'' +
            ", accountName='" + accountName + '\'' +
            ", location='" + location + '\'' +
            ", token='" + token + '\'' +
            ", user=" + user +
            '}';
    }
}
