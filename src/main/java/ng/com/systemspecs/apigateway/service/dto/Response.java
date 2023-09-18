package ng.com.systemspecs.apigateway.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "customerCareReferenceId",
    "exchangeReference",
    "errorMessage",
    "errorCode",
    "errorId",
    "auditReferenceNumber",
    "done",
    "status"
})
@Generated("jsonschema2pojo")
public class Response {

    @JsonProperty("customerCareReferenceId")
    private Object customerCareReferenceId;
    @JsonProperty("exchangeReference")
    private String exchangeReference;
    @JsonProperty("errorMessage")
    private String errorMessage;
    @JsonProperty("errorCode")
    private Object errorCode;
    @JsonProperty("errorId")
    private String errorId;
    @JsonProperty("auditReferenceNumber")
    private String auditReferenceNumber;
    @JsonProperty("done")
    private String done;
    @JsonProperty("status")
    private String status;

    @JsonProperty("customerCareReferenceId")
    public Object getCustomerCareReferenceId() {
        return customerCareReferenceId;
    }

    @JsonProperty("customerCareReferenceId")
    public void setCustomerCareReferenceId(Object customerCareReferenceId) {
        this.customerCareReferenceId = customerCareReferenceId;
    }

    @JsonProperty("exchangeReference")
    public String getExchangeReference() {
        return exchangeReference;
    }

    @JsonProperty("exchangeReference")
    public void setExchangeReference(String exchangeReference) {
        this.exchangeReference = exchangeReference;
    }

    @JsonProperty("errorMessage")
    public String getErrorMessage() {
        return errorMessage;
    }

    @JsonProperty("errorMessage")
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @JsonProperty("errorCode")
    public Object getErrorCode() {
        return errorCode;
    }

    @JsonProperty("errorCode")
    public void setErrorCode(Object errorCode) {
        this.errorCode = errorCode;
    }

    @JsonProperty("errorId")
    public String getErrorId() {
        return errorId;
    }

    @JsonProperty("errorId")
    public void setErrorId(String errorId) {
        this.errorId = errorId;
    }

    @JsonProperty("auditReferenceNumber")
    public String getAuditReferenceNumber() {
        return auditReferenceNumber;
    }

    @JsonProperty("auditReferenceNumber")
    public void setAuditReferenceNumber(String auditReferenceNumber) {
        this.auditReferenceNumber = auditReferenceNumber;
    }

    @JsonProperty("done")
    public String getDone() {
        return done;
    }

    @JsonProperty("done")
    public void setDone(String done) {
        this.done = done;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Response{" +
            "customerCareReferenceId=" + customerCareReferenceId +
            ", exchangeReference='" + exchangeReference + '\'' +
            ", errorMessage='" + errorMessage + '\'' +
            ", errorCode=" + errorCode +
            ", errorId='" + errorId + '\'' +
            ", auditReferenceNumber='" + auditReferenceNumber + '\'' +
            ", done='" + done + '\'' +
            ", status='" + status + '\'' +
            '}';
    }
}
