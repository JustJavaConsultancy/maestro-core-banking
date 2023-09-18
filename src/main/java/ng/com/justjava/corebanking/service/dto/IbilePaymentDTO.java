package ng.com.justjava.corebanking.service.dto;

public class IbilePaymentDTO {

    private String walletAccountNo;
    private Double walletBalance;
    private String payerId;
    private String payerName;
    private String billType;
    private String billReferenceNo;
    private Double billOutstandingAmt;
    private Double totalDue;
    private Double amountToPay;
    private String creditAccount;
    private String webGuid;

    public String getWalletAccountNo() {
        return walletAccountNo;
    }

    public void setWalletAccountNo(String walletAccountNo) {
        this.walletAccountNo = walletAccountNo;
    }

    public Double getWalletBalance() {
        return walletBalance;
    }

    public void setWalletBalance(Double walletBalance) {
        this.walletBalance = walletBalance;
    }

    public String getPayerId() {
        return payerId;
    }

    public void setPayerId(String payerId) {
        this.payerId = payerId;
    }

    public String getPayerName() {
        return payerName;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public String getBillReferenceNo() {
        return billReferenceNo;
    }

    public void setBillReferenceNo(String billReferenceNo) {
        this.billReferenceNo = billReferenceNo;
    }

    public Double getBillOutstandingAmt() {
        return billOutstandingAmt;
    }

    public void setBillOutstandingAmt(Double billOutstandingAmt) {
        this.billOutstandingAmt = billOutstandingAmt;
    }

    public Double getAmountToPay() {
        return amountToPay;
    }

    public void setAmountToPay(Double amountToPay) {
        if (amountToPay == null) {
            amountToPay = 0.0;
        }
        this.amountToPay = amountToPay;
    }

    public String getCreditAccount() {
        return creditAccount;
    }

    public void setCreditAccount(String creditAccount) {
        this.creditAccount = creditAccount;
    }

    public Double getTotalDue() {
        return totalDue;
    }

    public void setTotalDue(Double totalDue) {
        this.totalDue = totalDue;
    }

    public String getWebGuid() {
        return webGuid;
    }

    public void setWebGuid(String webGuid) {
        this.webGuid = webGuid;
    }

    @Override
    public String toString() {
        return "IbilePaymentDTO{" +
            "walletAccountNo='" + walletAccountNo + '\'' +
            ", walletBalance=" + walletBalance +
            ", payerId='" + payerId + '\'' +
            ", payerName='" + payerName + '\'' +
            ", billType='" + billType + '\'' +
            ", billReferenceNo='" + billReferenceNo + '\'' +
            ", billOutstandingAmt=" + billOutstandingAmt +
            ", amountToPay=" + amountToPay +
            ", creditAccount=" + creditAccount +
            ", totalDue=" + totalDue +
            ", webGuid=" + webGuid +
            '}';
    }
}
