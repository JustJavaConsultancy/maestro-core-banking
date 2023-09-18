package ng.com.justjava.corebanking.service.dto;

import ng.com.systemspecs.remitabillinggateway.validate.ValidateRequest;

import java.io.Serializable;

public class PayBillDTO implements Serializable {


    private String pin;

    private String sourceAccountNo;

    private ValidateRequest validateRequest;

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


    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public ValidateRequest getValidateRequest() {
        return validateRequest;
    }

    public String getSourceAccountNo() {
        return sourceAccountNo;
    }

    public void setSourceAccountNo(String sourceAccountNo) {
        this.sourceAccountNo = sourceAccountNo;
    }

    public void setValidateRequest(ValidateRequest validateRequest) {
        this.validateRequest = validateRequest;
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

    public String getAgentRef() {
        return agentRef;
    }

    public void setAgentRef(String agentRef) {
        this.agentRef = agentRef;
    }

    @Override
    public String toString() {
        return "PayBillDTO{" +
            "pin='" + pin + '\'' +
            ", sourceAccountNo='" + sourceAccountNo + '\'' +
            ", validateRequest=" + validateRequest +
            ", transRef='" + transRef + '\'' +
            ", narration='" + narration + '\'' +
            ", agentRef='" + agentRef + '\'' +
            '}';
    }
}
