package ng.com.systemspecs.apigateway.service.dto.stp;


import java.io.Serializable;


@SuppressWarnings("serial")
public class NameEnquiryResponse implements Serializable {

    private String responseCode;
    private String responseMessage;
    private String accountName;
    private String accountNumber;
    private String signatories = null;

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

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getSignatories() {
        return signatories;
    }

    public void setSignatories(String signatories) {
        this.signatories = signatories;
    }

    @Override
    public String toString() {
        return "NameEnquiryResponse{" +
            "responseCode='" + responseCode + '\'' +
            ", responseMessage='" + responseMessage + '\'' +
            ", accountName='" + accountName + '\'' +
            ", accountNumber='" + accountNumber + '\'' +
            ", signatories='" + signatories + '\'' +
            '}';
    }
}
