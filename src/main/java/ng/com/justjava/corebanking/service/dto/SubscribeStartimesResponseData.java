package ng.com.justjava.corebanking.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "error",
    "smartCardCode",
    "name",
    "bouquet",
    "externalReference",
    "amount",
    "message",
    "description",
    "responseCode",
    "reference",
    "sequence",
    "clientReference"
})
@Generated("jsonschema2pojo")
public class SubscribeStartimesResponseData {

    @JsonProperty("status")
    private String status;
    @JsonProperty("error")
    private Boolean error;
    @JsonProperty("smartCardCode")
    private String smartCardCode;
    @JsonProperty("name")
    private String name;
    @JsonProperty("bouquet")
    private String bouquet;
    @JsonProperty("externalReference")
    private String externalReference;
    @JsonProperty("amount")
    private Integer amount;
    @JsonProperty("message")
    private String message;
    @JsonProperty("description")
    private String description;
    @JsonProperty("responseCode")
    private String responseCode;
    @JsonProperty("reference")
    private String reference;
    @JsonProperty("sequence")
    private String sequence;
    @JsonProperty("clientReference")
    private String clientReference;

    @JsonProperty("error")
    public Boolean getError() {
        return error;
    }

    @JsonProperty("error")
    public void setError(Boolean error) {
        this.error = error;
    }

    @JsonProperty("smartCardCode")
    public String getSmartCardCode() {
        return smartCardCode;
    }

    @JsonProperty("smartCardCode")
    public void setSmartCardCode(String smartCardCode) {
        this.smartCardCode = smartCardCode;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("bouquet")
    public String getBouquet() {
        return bouquet;
    }

    @JsonProperty("bouquet")
    public void setBouquet(String bouquet) {
        this.bouquet = bouquet;
    }

    @JsonProperty("externalReference")
    public String getExternalReference() {
        return externalReference;
    }

    @JsonProperty("externalReference")
    public void setExternalReference(String externalReference) {
        this.externalReference = externalReference;
    }

    @JsonProperty("amount")
    public Integer getAmount() {
        return amount;
    }

    @JsonProperty("amount")
    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    @JsonProperty("message")
    public void setMessage(String message) {
        this.message = message;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("responseCode")
    public String getResponseCode() {
        return responseCode;
    }

    @JsonProperty("responseCode")
    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    @JsonProperty("reference")
    public String getReference() {
        return reference;
    }

    @JsonProperty("reference")
    public void setReference(String reference) {
        this.reference = reference;
    }

    @JsonProperty("sequence")
    public String getSequence() {
        return sequence;
    }

    @JsonProperty("sequence")
    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    @JsonProperty("clientReference")
    public String getClientReference() {
        return clientReference;
    }

    @JsonProperty("clientReference")
    public void setClientReference(String clientReference) {
        this.clientReference = clientReference;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "SubscribeStartimesResponseData{" +
            "status=" + status +
            "error=" + error +
            ", smartCardCode='" + smartCardCode + '\'' +
            ", name='" + name + '\'' +
            ", bouquet='" + bouquet + '\'' +
            ", externalReference='" + externalReference + '\'' +
            ", amount=" + amount +
            ", message='" + message + '\'' +
            ", description='" + description + '\'' +
            ", responseCode='" + responseCode + '\'' +
            ", reference='" + reference + '\'' +
            ", sequence='" + sequence + '\'' +
            ", clientReference='" + clientReference + '\'' +
            '}';
    }
}
