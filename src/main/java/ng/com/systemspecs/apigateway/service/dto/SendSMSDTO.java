package ng.com.systemspecs.apigateway.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SendSMSDTO {

    private String mobileNumber;
    private String smsMessage;

//    @JsonProperty("smsuserid")
    private String smsUserId = "POUCHII";

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getSmsMessage() {
        return smsMessage;
    }

    public void setSmsMessage(String smsMessage) {
        this.smsMessage = smsMessage;
    }

       public String getSmsUserId() {
           return smsUserId;
       }

        public void setSmsUserId(String smsUserId) {
            this.smsUserId = smsUserId;
        }
    @Override
    public String toString() {
        return "SendSMSDTO{" +
            "mobileNumber='" + mobileNumber + '\'' +
            ", smsMessage='" + smsMessage + '\'' +
            ", smsUserId='" + smsUserId + '\'' +
            '}';
    }
}
