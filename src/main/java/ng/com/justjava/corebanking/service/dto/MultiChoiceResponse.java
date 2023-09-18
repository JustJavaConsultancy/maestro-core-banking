package ng.com.justjava.corebanking.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "statusCode",
    "transactionStatus",
    "transactionReference",
    "transactionMessage",
    "baxiReference",
    "provider_message"
})
@Generated("jsonschema2pojo")
public class MultiChoiceResponse {

    @JsonProperty("statusCode")
    private String statusCode;
    @JsonProperty("transactionStatus")
    private String transactionStatus;
    @JsonProperty("transactionReference")
    private Integer transactionReference;
    @JsonProperty("transactionMessage")
    private String transactionMessage;
    @JsonProperty("baxiReference")
    private Integer baxiReference;
    @JsonProperty("provider_message")
    private String providerMessage;

    @JsonProperty("statusCode")
    public String getStatusCode() {
        return statusCode;
    }

    @JsonProperty("statusCode")
    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    @JsonProperty("transactionStatus")
    public String getTransactionStatus() {
        return transactionStatus;
    }

    @JsonProperty("transactionStatus")
    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    @JsonProperty("transactionReference")
    public Integer getTransactionReference() {
        return transactionReference;
    }

    @JsonProperty("transactionReference")
    public void setTransactionReference(Integer transactionReference) {
        this.transactionReference = transactionReference;
    }

    @JsonProperty("transactionMessage")
    public String getTransactionMessage() {
        return transactionMessage;
    }

    @JsonProperty("transactionMessage")
    public void setTransactionMessage(String transactionMessage) {
        this.transactionMessage = transactionMessage;
    }

    @JsonProperty("baxiReference")
    public Integer getBaxiReference() {
        return baxiReference;
    }

    @JsonProperty("baxiReference")
    public void setBaxiReference(Integer baxiReference) {
        this.baxiReference = baxiReference;
    }

    @JsonProperty("provider_message")
    public String getProviderMessage() {
        return providerMessage;
    }

    @JsonProperty("provider_message")
    public void setProviderMessage(String providerMessage) {
        this.providerMessage = providerMessage;
    }

    @Override
    public String toString() {
        return "MultiChoiceResponse{" +
            "statusCode:'" + statusCode + '\'' +
            ", transactionStatus:'" + transactionStatus + '\'' +
            ", transactionReference:" + transactionReference +
            ", transactionMessage:'" + transactionMessage + '\'' +
            ", baxiReference:" + baxiReference +
            ", providerMessage:'" + providerMessage + '\'' +
            '}';
    }
}
