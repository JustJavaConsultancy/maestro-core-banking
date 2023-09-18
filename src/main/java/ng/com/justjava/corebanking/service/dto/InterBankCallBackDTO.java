package ng.com.justjava.corebanking.service.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class InterBankCallBackDTO {

    @SerializedName("trackingRef")
    @Expose
    private String trackingRef;
    @SerializedName("sessionID")
    @Expose
    private String sessionID;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("code")
    @Expose
    private String code;

    public String getTrackingRef() {
        return trackingRef;
    }

    public void setTrackingRef(String trackingRef) {
        this.trackingRef = trackingRef;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "InterBankCallBackDTO{" +
            "trackingRef='" + trackingRef + '\'' +
            ", sessionID='" + sessionID + '\'' +
            ", message='" + message + '\'' +
            ", code='" + code + '\'' +
            '}';
    }
}
