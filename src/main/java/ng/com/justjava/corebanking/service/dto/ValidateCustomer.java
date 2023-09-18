package ng.com.justjava.corebanking.service.dto;

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
    "customerId",
    "billPaymentProductId",
    "name",
    "email",
    "address",
    "amount",
    "maximumAmount",
    "minimumAmount"
})
@Generated("jsonschema2pojo")
public class ValidateCustomer {

    @JsonProperty("customerId")
    private Object customerId;
    @JsonProperty("billPaymentProductId")
    private String billPaymentProductId;
    @JsonProperty("name")
    private String name;
    @JsonProperty("email")
    private String email;
    @JsonProperty("address")
    private String address;
    @JsonProperty("amount")
    private Double amount;
    @JsonProperty("maximumAmount")
    private Double maximumAmount;
    @JsonProperty("minimumAmount")
    private Double minimumAmount;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("customerId")
    public Object getCustomerId() {
        return customerId;
    }

    @JsonProperty("customerId")
    public void setCustomerId(Object customerId) {
        this.customerId = customerId;
    }

    @JsonProperty("billPaymentProductId")
    public String getBillPaymentProductId() {
        return billPaymentProductId;
    }

    @JsonProperty("billPaymentProductId")
    public void setBillPaymentProductId(String billPaymentProductId) {
        this.billPaymentProductId = billPaymentProductId;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    @JsonProperty("email")
    public void setEmail(String email) {
        this.email = email;
    }

    @JsonProperty("address")
    public String getAddress() {
        return address;
    }

    @JsonProperty("address")
    public void setAddress(String address) {
        this.address = address;
    }

    @JsonProperty("amount")
    public Double getAmount() {
        return amount;
    }

    @JsonProperty("amount")
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @JsonProperty("maximumAmount")
    public Double getMaximumAmount() {
        return maximumAmount;
    }

    @JsonProperty("maximumAmount")
    public void setMaximumAmount(Double maximumAmount) {
        this.maximumAmount = maximumAmount;
    }

    @JsonProperty("minimumAmount")
    public Double getMinimumAmount() {
        return minimumAmount;
    }

    @JsonProperty("minimumAmount")
    public void setMinimumAmount(Double minimumAmount) {
        this.minimumAmount = minimumAmount;
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
        return "ValidateCustomer{" +
            "customerId=" + customerId +
            ", billPaymentProductId='" + billPaymentProductId + '\'' +
            ", name='" + name + '\'' +
            ", email='" + email + '\'' +
            ", address='" + address + '\'' +
            ", amount=" + amount +
            ", maximumAmount=" + maximumAmount +
            ", minimumAmount=" + minimumAmount +
            ", additionalProperties=" + additionalProperties +
            '}';
    }
}
