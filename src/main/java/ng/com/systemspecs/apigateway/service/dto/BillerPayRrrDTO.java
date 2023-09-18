package ng.com.systemspecs.apigateway.service.dto;

public class BillerPayRrrDTO {

    private String sourceAccount;
    private String totalAmount;
    private double bonusAmount;
    private String transactionRef;
    private String narration;
    private String phoneNumber;
    private String rrr;
    private boolean redeemBonus;

    public String getSourceAccount() {
        return sourceAccount;
    }

    public void setSourceAccount(String sourceAccount) {
        this.sourceAccount = sourceAccount;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getBonusAmount() {
        return bonusAmount;
    }

    public void setBonusAmount(double bonusAmount) {
        this.bonusAmount = bonusAmount;
    }

    public String getTransactionRef() {
        return transactionRef;
    }

    public void setTransactionRef(String transactionRef) {
        this.transactionRef = transactionRef;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRrr() {
        return rrr;
    }

    public void setRrr(String rrr) {
        this.rrr = rrr;
    }

    public boolean isRedeemBonus() {
        return redeemBonus;
    }

    public void setRedeemBonus(boolean redeemBonus) {
        this.redeemBonus = redeemBonus;
    }

    @Override
    public String toString() {
        return "BillerPayRRRDTO{" +
            "sourceAccount='" + sourceAccount + '\'' +
            ", totalAmount='" + totalAmount + '\'' +
            ", bonusAmount=" + bonusAmount +
            ", transactionRef='" + transactionRef + '\'' +
            ", narration='" + narration + '\'' +
            ", phoneNumber='" + phoneNumber + '\'' +
            ", rrr='" + rrr + '\'' +
            ", redeemBonus=" + redeemBonus +
            '}';
    }
}
