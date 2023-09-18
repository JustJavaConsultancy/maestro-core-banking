package ng.com.systemspecs.apigateway.service.dto.stp;


import java.io.Serializable;


@SuppressWarnings("serial")
public class GenerateOTPResponse implements Serializable {

    private String responseCode;
    private String responseMessage;
    private String accountClass;
    private String phoneNumber;

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

    public String getAccountClass() {
        return accountClass;
    }

    public void setAccountClass(String accountClass) {
        this.accountClass = accountClass;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "GenerateOTPResponse{" +
            "responseCode='" + responseCode + '\'' +
            ", responseMessage='" + responseMessage + '\'' +
            ", accountClass='" + accountClass + '\'' +
            ", phoneNumber='" + phoneNumber + '\'' +
            '}';
    }
}
