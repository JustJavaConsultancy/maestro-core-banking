package ng.com.justjava.corebanking.service.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class BuyAirtimeDTO {

    @NotBlank
    private String serviceId;

    @Size(min = 11, max = 15, message = "phone number must be greater than 10 and less than 15 characters")
    private String phoneNumber;

    @NotBlank
    private String customFieldId;

    @NotBlank
    private String pin;

    @Min(value = 1, message = "Amount cannot be negative")
    private Double amount;

    @NotBlank
    private String sourceAccountNo;

    private String transRef;

    private String narration;

    private String agentRef;

    private boolean redeemBonus;

    private double bonusAmount;

    public double getBonusAmount() {
        return bonusAmount;
    }

    public void setBonusAmount(double bonusAmount) {
        this.bonusAmount = bonusAmount;
    }

    public boolean isRedeemBonus() {
        return redeemBonus;
    }

    public void setRedeemBonus(boolean redeemBonus) {
        this.redeemBonus = redeemBonus;
    }


    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    public String getCustomFieldId() {
        return customFieldId;
    }

    public void setCustomFieldId(String customFieldId) {
        this.customFieldId = customFieldId;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getSourceAccountNo() {
        return sourceAccountNo;
    }

    public void setSourceAccountNo(String sourceAccountNo) {
        this.sourceAccountNo = sourceAccountNo;
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

    @Override
    public String toString() {

        return "BuyAirtimeDTO{" +
            "serviceId='" + serviceId + '\'' +
            ", phoneNumber='" + phoneNumber + '\'' +
            ", customFieldId='" + customFieldId + '\'' +
            ", pin='" + pin + '\'' +
            ", amount=" + amount +
            ", sourceAccountNo='" + sourceAccountNo + '\'' +
            ", transRef='" + transRef + '\'' +
            ", narration='" + narration + '\'' +
            ", agentRef='" + agentRef + '\'' +
            ", redeemBonus='" + redeemBonus + '\'' +
            ", bonusAmount='" + bonusAmount + '\'' +
            '}';

    }

    public String getAgentRef() {
        return agentRef;
    }

    public void setAgentRef(String agentTransRef) {
        this.agentRef = agentTransRef;
    }
}
