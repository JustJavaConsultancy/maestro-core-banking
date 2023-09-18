package ng.com.justjava.corebanking.service.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class CashConnectTransferStatus {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("sessionId")
    @Expose
    private String sessionId;
    @SerializedName("statusCode")
    @Expose
    private String statusCode;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public String toString() {
        return "CashConnectTransferStatus{" +
            "status='" + status + '\'' +
            ", message='" + message + '\'' +
            ", sessionId='" + sessionId + '\'' +
            ", statusCode='" + statusCode + '\'' +
            '}';
    }
}
