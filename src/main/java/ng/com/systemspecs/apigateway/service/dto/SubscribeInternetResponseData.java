package ng.com.systemspecs.apigateway.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "error",
    "message",
    "transactionID",
    "reference",
    "responseCode",
    "bundle",
    "amount"
})
@Generated("jsonschema2pojo")
public class SubscribeInternetResponseData {

    @JsonProperty("error")
    private Boolean error;
    @JsonProperty("message")
    private String message;
    @JsonProperty("transactionID")
    private String transactionID;
    @JsonProperty("reference")
    private String reference;
    @JsonProperty("responseCode")
    private String responseCode;
    @JsonProperty("bundle")
    private String bundle;
    @JsonProperty("amount")
    private Integer amount;

    @JsonProperty("error")
    public Boolean getError() {
        return error;
    }

    @JsonProperty("error")
    public void setError(Boolean error) {
        this.error = error;
    }

    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    @JsonProperty("message")
    public void setMessage(String message) {
        this.message = message;
    }

    @JsonProperty("transactionID")
    public String getTransactionID() {
        return transactionID;
    }

    @JsonProperty("transactionID")
    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    @JsonProperty("reference")
    public String getReference() {
        return reference;
    }

    @JsonProperty("reference")
    public void setReference(String reference) {
        this.reference = reference;
    }

    @JsonProperty("responseCode")
    public String getResponseCode() {
        return responseCode;
    }

    @JsonProperty("responseCode")
    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    @JsonProperty("bundle")
    public String getBundle() {
        return bundle;
    }

    @JsonProperty("bundle")
    public void setBundle(String bundle) {
        this.bundle = bundle;
    }

    @JsonProperty("amount")
    public Integer getAmount() {
        return amount;
    }

    @JsonProperty("amount")
    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "SubscribeInternetResponseData{" +
            "error=" + error +
            ", message='" + message + '\'' +
            ", transactionID='" + transactionID + '\'' +
            ", reference='" + reference + '\'' +
            ", responseCode='" + responseCode + '\'' +
            ", bundle='" + bundle + '\'' +
            ", amount=" + amount +
            '}';
    }
}
