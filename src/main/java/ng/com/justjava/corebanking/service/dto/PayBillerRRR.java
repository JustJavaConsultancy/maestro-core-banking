package ng.com.justjava.corebanking.service.dto;

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
    "description",
    "beneficiary",
    "service",
    "amount",
    "payerName",
    "payerPhoneNumber",
    "payerEmail"
})
@Generated("jsonschema2pojo")
public class PayBillerRRR implements Serializable {

    @JsonProperty("description")
    private String description;
    @JsonProperty("beneficiary")
    private String beneficiary;
    @JsonProperty("service")
    private String service;
    @JsonProperty("amount")
    private String amount;
    @JsonProperty("payerName")
    private String payerName;
    @JsonProperty("payerPhoneNumber")
    private String payerPhoneNumber;
    @JsonProperty("payerEmail")
    private String payerEmail;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        if (description == null) description = "N/A";
        this.description = description;
    }

    @JsonProperty("beneficiary")
    public String getBeneficiary() {
        return beneficiary;
    }

    @JsonProperty("beneficiary")
    public void setBeneficiary(String beneficiary) {
        if (beneficiary == null) beneficiary = "N/A";
        this.beneficiary = beneficiary;
    }

    @JsonProperty("service")
    public String getService() {
        return service;
    }

    @JsonProperty("service")
    public void setService(String service) {
        if (service == null) service = "N/A";
        this.service = service;
    }

    @JsonProperty("amount")
    public String getAmount() {
        return amount;
    }

    @JsonProperty("amount")
    public void setAmount(String amount) {
        this.amount = amount;
    }

    @JsonProperty("payerName")
    public String getPayerName() {
        return payerName;
    }

    @JsonProperty("payerName")
    public void setPayerName(String payerName) {
        if (payerName == null) payerName = "N/A";
        this.payerName = payerName;
    }

    @JsonProperty("payerPhoneNumber")
    public String getPayerPhoneNumber() {
        return payerPhoneNumber;
    }

    @JsonProperty("payerPhoneNumber")
    public void setPayerPhoneNumber(String payerPhoneNumber) {
        if (payerPhoneNumber == null) payerPhoneNumber = "N/A";
        this.payerPhoneNumber = payerPhoneNumber;
    }

    @JsonProperty("payerEmail")
    public String getPayerEmail() {
        return payerEmail;
    }

    @JsonProperty("payerEmail")
    public void setPayerEmail(String payerEmail) {
        if (payerEmail == null) payerEmail = "N/A";
        this.payerEmail = payerEmail;
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
        return "PayBillerRRR{" +
            "description='" + description + '\'' +
            ", beneficiary='" + beneficiary + '\'' +
            ", service='" + service + '\'' +
            ", amount='" + amount + '\'' +
            ", payerName='" + payerName + '\'' +
            ", payerPhoneNumber='" + payerPhoneNumber + '\'' +
            ", payerEmail='" + payerEmail + '\'' +
            ", additionalProperties=" + additionalProperties +
            '}';
    }
}
