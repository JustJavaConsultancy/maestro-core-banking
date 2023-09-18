package ng.com.systemspecs.apigateway.service.dto;

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
    "authorisationCode",
    "accountNumber",
    "bankCode",
    "amount",
    "customerId",
    "status",
    "mandateReference"
})
@Generated("jsonschema2pojo")
public class ApproveLoanResponseDataDTORemitaRespDataDTO {

    @JsonProperty("authorisationCode")
    private String authorisationCode;
    @JsonProperty("accountNumber")
    private String accountNumber;
    @JsonProperty("bankCode")
    private String bankCode;
    @JsonProperty("amount")
    private String amount;
    @JsonProperty("customerId")
    private String customerId;
    @JsonProperty("status")
    private Object status;
    @JsonProperty("mandateReference")
    private String mandateReference;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("authorisationCode")
    public String getAuthorisationCode() {
        return authorisationCode;
    }

    @JsonProperty("authorisationCode")
    public void setAuthorisationCode(String authorisationCode) {
        this.authorisationCode = authorisationCode;
    }

    @JsonProperty("accountNumber")
    public String getAccountNumber() {
        return accountNumber;
    }

    @JsonProperty("accountNumber")
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    @JsonProperty("bankCode")
    public String getBankCode() {
        return bankCode;
    }

    @JsonProperty("bankCode")
    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    @JsonProperty("amount")
    public String getAmount() {
        return amount;
    }

    @JsonProperty("amount")
    public void setAmount(String amount) {
        this.amount = amount;
    }

    @JsonProperty("customerId")
    public String getCustomerId() {
        return customerId;
    }

    @JsonProperty("customerId")
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    @JsonProperty("status")
    public Object getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(Object status) {
        this.status = status;
    }

    @JsonProperty("mandateReference")
    public String getMandateReference() {
        return mandateReference;
    }

    @JsonProperty("mandateReference")
    public void setMandateReference(String mandateReference) {
        this.mandateReference = mandateReference;
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
