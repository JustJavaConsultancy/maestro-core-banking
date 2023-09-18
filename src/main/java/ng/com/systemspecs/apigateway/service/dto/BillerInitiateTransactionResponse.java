package ng.com.systemspecs.apigateway.service.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "transactionRef",
    "rrr",
    "amount"
})
@Generated("jsonschema2pojo")
public class BillerInitiateTransactionResponse implements Serializable {

    @JsonProperty("transactionRef")
    private String transactionRef;
    @JsonProperty("rrr")
    private String rrr;
    @JsonProperty("amount")
    private Double amount;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("transactionRef")
    public String getTransactionRef() {
        return transactionRef;
    }

    @JsonProperty("transactionRef")
    public void setTransactionRef(String transactionRef) {
        this.transactionRef = transactionRef;
    }

    @JsonProperty("rrr")
    public String getRrr() {
        return rrr;
    }

    @JsonProperty("rrr")
    public void setRrr(String rrr) {
        this.rrr = rrr;
    }

    @JsonProperty("amount")
    public Double getAmount() {
        return amount;
    }

    @JsonProperty("amount")
    public void setAmount(Double amount) {
        this.amount = amount;
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
        return "BillerInitiateTransactionResponse{" +
            "transactionRef='" + transactionRef + '\'' +
            ", rrr='" + rrr + '\'' +
            ", amount=" + amount +
            ", additionalProperties=" + additionalProperties +
            '}';
    }
}
