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
    "interest_rate",
    "requested_amount",
    "duration"
})
@Generated("jsonschema2pojo")
public class CalculateRepaymentRequestDTO {

    @JsonProperty("interest_rate")
    private Double interestRate;
    @JsonProperty("requested_amount")
    private Double requestedAmount;
    @JsonProperty("duration")
    private Integer duration;

    @JsonProperty("interest_rate")
    public Double getInterestRate() {
        return interestRate;
    }

    @JsonProperty("interest_rate")
    public void setInterestRate(Double interestRate) {
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

    @JsonProperty("duration")
    public Integer getDuration() {
        return duration;
    }

    @JsonProperty("duration")
    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "CalculateRepaymentRequestDTO{" +
            "interestRate=" + interestRate +
            ", requestedAmount=" + requestedAmount +
            ", duration=" + duration +
            '}';
    }
}
