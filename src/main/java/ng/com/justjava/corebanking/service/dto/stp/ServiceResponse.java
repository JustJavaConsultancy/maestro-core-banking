package ng.com.justjava.corebanking.service.dto.stp;

import java.io.Serializable;


public class ServiceResponse implements Serializable {

    private String responseCode;

    private String responseMessage;

    private String transRef;


    public ServiceResponse() {
    }


    public ServiceResponse(String responseCode, String responseMessage) {
        this();
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
    }


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


    public String getTransRef() {
        return transRef;
    }


    public void setTransRef(String transRef) {
        this.transRef = transRef;
    }
}
