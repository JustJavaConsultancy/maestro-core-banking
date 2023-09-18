package ng.com.systemspecs.apigateway.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "error",
    "message",
    "amount",
    "ref",
    "date",
    "transactionID",
    "responseCode",
    "description",
    "reference"
})
@Generated("jsonschema2pojo")
public class SubscribeDataResponse {

    @JsonProperty("status")
    private String status;
    @JsonProperty("error")
    private Boolean error;
    @JsonProperty("message")
    private String message;
    @JsonProperty("amount")
    private Integer amount;
    @JsonProperty("ref")
    private String ref;
    @JsonProperty("date")
    private String date;
    @JsonProperty("transactionID")
    private String transactionID;
    @JsonProperty("responseCode")
    private String responseCode;
    @JsonProperty("description")
    private String description;
    @JsonProperty("reference")
    private String reference;


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

    @JsonProperty("amount")
    public Integer getAmount() {
        return amount;
    }

    @JsonProperty("amount")
    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    @JsonProperty("ref")
    public String getRef() {
        return ref;
    }

    @JsonProperty("ref")
    public void setRef(String ref) {
        this.ref = ref;
    }

    @JsonProperty("date")
    public String getDate() {
        return date;
    }

    @JsonProperty("date")
    public void setDate(String date) {
        this.date = date;
    }

    @JsonProperty("transactionID")
    public String getTransactionID() {
        return transactionID;
    }

    @JsonProperty("transactionID")
    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    @JsonProperty("responseCode")
    public String getResponseCode() {
        return responseCode;
    }

    @JsonProperty("responseCode")
    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("reference")
    public String getReference() {
        return reference;
    }

    @JsonProperty("reference")
    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "SubscribeDataResponse{" +
            "status=" + status +
            "error=" + error +
            ", message='" + message + '\'' +
            ", amount=" + amount +
            ", ref='" + ref + '\'' +
            ", date='" + date + '\'' +
            ", transactionID='" + transactionID + '\'' +
            ", responseCode='" + responseCode + '\'' +
            ", description='" + description + '\'' +
            ", reference='" + reference + '\'' +
            '}';
    }
}
