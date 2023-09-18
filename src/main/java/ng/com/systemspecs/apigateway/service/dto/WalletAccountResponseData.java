package ng.com.systemspecs.apigateway.service.dto;

public class WalletAccountResponseData {

    private String processCode;
    private String processMessage;
    private String requestId;
    private String accountNumber;
    private String customerId;
    private String accountOpeningDate;


    public String getProcessCode() {
        return processCode;
    }

    public void setProcessCode(String processCode) {
        this.processCode = processCode;
    }

    public String getProcessMessage() {
        return processMessage;
    }

    public void setProcessMessage(String processMessage) {
	this.processMessage = processMessage;
}
public String getRequestId() {
	return requestId;
}
public void setRequestId(String requestId) {
	this.requestId = requestId;
}

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getAccountOpeningDate() {
        return accountOpeningDate;
    }

    public void setAccountOpeningDate(String accountOpeningDate) {
        this.accountOpeningDate = accountOpeningDate;
    }

    @Override
    public String toString() {
        return "WalletAccountResponseData{" +
            "processCode='" + processCode + '\'' +
            ", processMessage='" + processMessage + '\'' +
            ", requestId='" + requestId + '\'' +
            ", accountNumber='" + accountNumber + '\'' +
            ", customerId='" + customerId + '\'' +
            ", accountOpeningDate='" + accountOpeningDate + '\'' +
            '}';
    }
}
