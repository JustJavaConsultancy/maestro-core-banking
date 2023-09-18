package ng.com.justjava.corebanking.service.dto;


import com.fasterxml.jackson.annotation.*;

import javax.annotation.Generated;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "WebGuid",
    "ResponseCode",
    "ResponseDesc",
    "ReceiptNumber",
    "State",
    "Status",
    "TransID",
    "TransCode",
    "StatusMessage",
    "Receipt"
})
@Generated("jsonschema2pojo")
public class PaymentNotificationResponseDTO {

    @JsonProperty("WebGuid")
    private String webGuid;
    @JsonProperty("ResponseCode")
    private String responseCode;
    @JsonProperty("ResponseDesc")
    private String responseDesc;
    @JsonProperty("ReceiptNumber")
    private String receiptNumber;
    @JsonProperty("State")
    private String state;
    @JsonProperty("Status")
    private String status;
    @JsonProperty("TransID")
    private String transID;
    @JsonProperty("TransCode")
    private String transCode;
    @JsonProperty("StatusMessage")
    private String statusMessage;
    @JsonProperty("Receipt")
    private List<Object> receipt = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("WebGuid")
    public String getWebGuid() {
        return webGuid;
    }

    @JsonProperty("WebGuid")
    public void setWebGuid(String webGuid) {
        this.webGuid = webGuid;
    }

    @JsonProperty("ResponseCode")
    public String getResponseCode() {
        return responseCode;
    }

    @JsonProperty("ResponseCode")
    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    @JsonProperty("ResponseDesc")
    public String getResponseDesc() {
        return responseDesc;
    }

    @JsonProperty("ResponseDesc")
    public void setResponseDesc(String responseDesc) {
        this.responseDesc = responseDesc;
    }

    @JsonProperty("ReceiptNumber")
    public String getReceiptNumber() {
        return receiptNumber;
    }

    @JsonProperty("ReceiptNumber")
    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    @JsonProperty("State")
    public String getState() {
        return state;
    }

    @JsonProperty("State")
    public void setState(String state) {
        this.state = state;
    }

    @JsonProperty("Status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("Status")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("TransID")
    public String getTransID() {
        return transID;
    }

    @JsonProperty("TransID")
    public void setTransID(String transID) {
        this.transID = transID;
    }

    @JsonProperty("TransCode")
    public String getTransCode() {
        return transCode;
    }

    @JsonProperty("TransCode")
    public void setTransCode(String transCode) {
        this.transCode = transCode;
    }

    @JsonProperty("StatusMessage")
    public String getStatusMessage() {
        return statusMessage;
    }

    @JsonProperty("StatusMessage")
    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    @JsonProperty("Receipt")
    public List<Object> getReceipt() {
        return receipt;
    }

    @JsonProperty("Receipt")
    public void setReceipt(List<Object> receipt) {
        this.receipt = receipt;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public String toString() {
        return "PaymentNotificationResponseDTO{" +
            "webGuid='" + webGuid + '\'' +
            ", responseCode='" + responseCode + '\'' +
            ", responseDesc='" + responseDesc + '\'' +
            ", receiptNumber='" + receiptNumber + '\'' +
            ", state='" + state + '\'' +
            ", status='" + status + '\'' +
            ", transID='" + transID + '\'' +
            ", transCode='" + transCode + '\'' +
            ", statusMessage='" + statusMessage + '\'' +
            ", receipt=" + receipt +
            ", additionalProperties=" + additionalProperties +
            '}';
    }
}
