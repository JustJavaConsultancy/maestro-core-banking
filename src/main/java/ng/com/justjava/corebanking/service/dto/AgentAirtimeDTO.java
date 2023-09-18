package ng.com.justjava.corebanking.service.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class AgentAirtimeDTO {

    private String accountNumber;
    @Min(value = 1, message = "Amount cannot be negative")
    private Double amount;
    private String channel;
    private String sourceAccountNumber;
    @Size(min = 11, max = 15, message = "phone number must be greater than 10 and less than 15 characters")
    private String phoneNumber;
    private String transRef;
    private String narration;

    private String customFieldId;
    @NotBlank
    private String serviceId;

    private String phoneNumberToCredit;

    private String pin;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getSourceAccountNumber() {
        return sourceAccountNumber;
    }

    public void setSourceAccountNumber(String sourceAccountNumber) {
        this.sourceAccountNumber = sourceAccountNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getTransRef() {
        return transRef;
    }

    public void setTransRef(String transRef) {
        this.transRef = transRef;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getPhoneNumberToCredit() {
        return phoneNumberToCredit;
    }

    public void setPhoneNumberToCredit(String phoneNumberToCredit) {
        this.phoneNumberToCredit = phoneNumberToCredit;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getCustomFieldId() {
        return customFieldId;
    }

    public void setCustomFieldId(String customFieldId) {
        this.customFieldId = customFieldId;
    }

    @Override
    public String toString() {

        return "AgentAirtimeDTO{" +
            "accountNumber='" + accountNumber + '\'' +
            ", amount=" + amount +
            ", channel='" + channel + '\'' +
            ", sourceAccountNumber='" + sourceAccountNumber + '\'' +
            ", phoneNumber='" + phoneNumber + '\'' +
            ", transRef='" + transRef + '\'' +
            ", narration='" + narration + '\'' +
            ", serviceId='" + serviceId + '\'' +
            ", phoneNumberToCredit='" + phoneNumberToCredit + '\'' +
            ", pin='" + pin + '\'' +
            ", customFieldId='" + customFieldId + '\'' +
            '}';

    }
}
