package ng.com.systemspecs.apigateway.service.dto;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "status",
    "loan_fee",
    "phone",
    "data"
})
@Generated("jsonschema2pojo")
public class EligibilityResponseNew {

    @JsonProperty("status")
    private Integer status;
    @JsonProperty("loan_fee")
    private Double loanFee;
    @JsonProperty("phone")
    private String phoneNumber;
    @JsonProperty("data")
    private List<EligibilityResponseData> data = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("status")
    public Integer getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(Integer status) {
        this.status = status;
    }

    @JsonProperty("loan_fee")
    public Double getLoanFee() {
        return loanFee;
    }

    @JsonProperty("loan_fee")
    public void setLoanFee(Double loanFee) {
        this.loanFee = loanFee;
    }

    @JsonProperty("phone")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @JsonProperty("phone")
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @JsonProperty("data")
    public List<EligibilityResponseData> getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(List<EligibilityResponseData> data) {
        this.data = data;
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
