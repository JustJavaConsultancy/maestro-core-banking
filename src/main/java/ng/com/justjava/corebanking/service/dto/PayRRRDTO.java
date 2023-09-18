package ng.com.justjava.corebanking.service.dto;

import java.io.Serializable;

public class PayRRRDTO implements Serializable {

    private String pin;
    private String rrr;
    private Double amount;
    private String sourceAccountNo;
    private String serviceTypeId;
    private String transRef;
    private String narration;
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

    public String getRrr() {
        return rrr;
    }
	public void setRrr(String rrr) {
		this.rrr = rrr;
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

    public String getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(String serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
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

        return "PayRRRDTO{" +
            "pin='" + pin + '\'' +
            ", rrr='" + rrr + '\'' +
            ", amount=" + amount +
            ", sourceAccountNo='" + sourceAccountNo + '\'' +
            ", serviceTypeId='" + serviceTypeId + '\'' +
            ", transRef='" + transRef + '\'' +
            ", narration='" + narration + '\'' +
            ", redeemBonus='" + redeemBonus + '\'' +
            ", bonusAmount='" + bonusAmount + '\'' +
            '}';
    }
}
