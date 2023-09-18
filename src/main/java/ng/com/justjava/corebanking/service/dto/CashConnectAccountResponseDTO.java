package ng.com.justjava.corebanking.service.dto;

import com.fasterxml.jackson.annotation.*;

import javax.annotation.Generated;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "IsSuccessful",
    "CustomerIDInString",
    "Message",
    "TransactionTrackingRef",
    "Page"
})
@Generated("jsonschema2pojo")
public class CashConnectAccountResponseDTO {

    @JsonProperty("IsSuccessful")
    private Boolean isSuccessful;
    @JsonProperty("CustomerIDInString")
    private Object customerIDInString;
    @JsonProperty("Message")
    private String message;
    @JsonProperty("TransactionTrackingRef")
    private String transactionTrackingRef;
    @JsonProperty("Page")
    private Object page;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("IsSuccessful")
    public Boolean getIsSuccessful() {
        return isSuccessful;
    }

    @JsonProperty("IsSuccessful")
    public void setIsSuccessful(Boolean isSuccessful) {
        this.isSuccessful = isSuccessful;
    }

    @JsonProperty("CustomerIDInString")
    public Object getCustomerIDInString() {
        return customerIDInString;
    }

    @JsonProperty("CustomerIDInString")
    public void setCustomerIDInString(Object customerIDInString) {
        this.customerIDInString = customerIDInString;
    }

    @JsonProperty("Message")
    public String getMessage() {
        return message;
    }

    @JsonProperty("Message")
    public void setMessage(String message) {
        this.message = message;
    }

    @JsonProperty("TransactionTrackingRef")
    public String getTransactionTrackingRef() {
        return transactionTrackingRef;
    }

    @JsonProperty("TransactionTrackingRef")
    public void setTransactionTrackingRef(String transactionTrackingRef) {
        this.transactionTrackingRef = transactionTrackingRef;
    }

    @JsonProperty("Page")
    public Object getPage() {
        return page;
    }

    @JsonProperty("Page")
    public void setPage(Object page) {
        this.page = page;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
