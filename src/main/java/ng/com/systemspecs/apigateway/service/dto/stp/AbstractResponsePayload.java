package ng.com.systemspecs.apigateway.service.dto.stp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ng.com.systemspecs.apigateway.domain.enumeration.RequestCode;
import ng.com.systemspecs.apigateway.domain.enumeration.ResponseCode;

import java.io.Serializable;

@SuppressWarnings("serial")
@JsonIgnoreProperties(ignoreUnknown = true)
abstract class AbstractResponsePayload implements Serializable {
    private RequestCode requestCode;
    private String responseCode;
    private String responseMessage;
    private long timestamp;
    private ErrorDetails error;
    private String bank;

    public AbstractResponsePayload() {
        super();

        this.timestamp = System.currentTimeMillis();
        this.responseCode = ResponseCode.ERROR.getCode();
    }

    public AbstractResponsePayload(RequestCode requestCode) {
        this();

        this.requestCode = requestCode;
    }

    public RequestCode getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(RequestCode requestCode) {
        this.requestCode = requestCode;
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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public ErrorDetails getError() {
        return error;
    }

    public void setError(ErrorDetails error) {
        this.error = error;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }
}
