package ng.com.systemspecs.apigateway.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoanPaymentNotificationDTO {

    private String accountNumber;
    @JsonProperty("PhoneNo")
    private String PhoneNo;
    @JsonProperty("LoanId")
    private String LoanId;
    private Double amount;
    private Double paymentRef;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    @JsonProperty("PhoneNo")
    public String getPhoneNo() {
        return PhoneNo;
    }

    @JsonProperty("PhoneNo")
    public void setPhoneNo(String phoneNo) {
        PhoneNo = phoneNo;
    }

    @JsonProperty("LoanId")
    public String getLoanId() {
        return LoanId;
    }

    @JsonProperty("LoanId")
    public void setLoanId(String loanId) {
        LoanId = loanId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getPaymentRef() {
        return paymentRef;
    }

    public void setPaymentRef(Double paymentRef) {
        this.paymentRef = paymentRef;
    }

    @Override
    public String toString() {
        return "LoanPaymentNotificationDTO{" +
            "accountNumber='" + accountNumber + '\'' +
            ", PhoneNo='" + PhoneNo + '\'' +
            ", LoanId='" + LoanId + '\'' +
            ", amount=" + amount +
            ", paymentRef=" + paymentRef +
            '}';
    }
}
