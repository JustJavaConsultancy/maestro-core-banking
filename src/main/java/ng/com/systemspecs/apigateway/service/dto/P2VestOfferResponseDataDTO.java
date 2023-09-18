package ng.com.systemspecs.apigateway.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "repayment_amount",
    "interest_rate"
})
@Generated("jsonschema2pojo")
public class P2VestOfferResponseDataDTO {

    @JsonProperty("repayment_amount")
    private Double repayment_amount;
    @JsonProperty("interest_rate")
    private Double interest_rate;

    @JsonProperty("repayment_amount")
    public Double getRepayment_amount() {
        return repayment_amount;
    }

    @JsonProperty("repayment_amount")
    public void setRepayment_amount(Double repayment_amount) {
        this.repayment_amount = repayment_amount;
    }

    @JsonProperty("interest_rate")
    public Double getInterest_rate() {
        return interest_rate;
    }

    @JsonProperty("interest_rate")
    public void setInterest_rate(Double interest_rate) {
        this.interest_rate = interest_rate;
    }

    @Override
    public String toString() {
        return "P2VestOfferResponseDataDTO{" +
            "repayment_amount=" + repayment_amount +
            ", interest_rate=" + interest_rate +
            '}';
    }
}
