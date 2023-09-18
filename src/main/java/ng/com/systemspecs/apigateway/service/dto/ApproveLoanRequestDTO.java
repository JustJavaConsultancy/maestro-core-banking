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
    "borrower_phone",
    "duration",
    "interest_rate",
    "requested_amount"
})
@Generated("jsonschema2pojo")
public class ApproveLoanRequestDTO {

    @JsonProperty("borrower_phone")
    private String borrowerPhone;
    @JsonProperty("duration")
    private Integer duration;
    @JsonProperty("interest_rate")
    private Integer interestRate;
    @JsonProperty("requested_amount")
    private Double requestedAmount;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("borrower_phone")
    public String getBorrowerPhone() {
        return borrowerPhone;
    }

    @JsonProperty("borrower_phone")
    public void setBorrowerPhone(String borrowerPhone) {
        this.borrowerPhone = borrowerPhone;
    }

    @JsonProperty("duration")
    public Integer getDuration() {
        return duration;
    }

    @JsonProperty("duration")
    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    @JsonProperty("interest_rate")
    public Integer getInterestRate() {
        return interestRate;
    }

    @JsonProperty("interest_rate")
    public void setInterestRate(Integer interestRate) {
        this.interestRate = interestRate;
    }

    @JsonProperty("requested_amount")
    public Double getRequestedAmount() {
        return requestedAmount;
    }

    @JsonProperty("requested_amount")
    public void setRequestedAmount(Double requestedAmount) {
        this.requestedAmount = requestedAmount;
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
