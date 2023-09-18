package ng.com.systemspecs.apigateway.service.dto.stp;


import java.io.Serializable;


@SuppressWarnings("serial")
public class BalanceEnquiryResponse implements Serializable {

    private String responseCode;
    private String responseMessage;
    private String accountName;
    private String balanceDate;
    private String amount;
    private String currency;

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getBalanceDate() {
        return balanceDate;
    }

    public void setBalanceDate(String balanceDate) {
        this.balanceDate = balanceDate;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }


    @Override
    public String toString() {
        return "BalanceEnquiryResponse{" +
            "responseCode='" + responseCode + '\'' +
            ", responseMessage='" + responseMessage + '\'' +
            ", accountName='" + accountName + '\'' +
            ", balanceDate=" + balanceDate +
            ", amount='" + amount + '\'' +
            ", currency='" + currency + '\'' +
            '}';
    }
}
