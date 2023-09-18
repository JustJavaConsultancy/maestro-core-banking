package ng.com.justjava.corebanking.service.dto;

    import javax.annotation.Generated;
    import com.fasterxml.jackson.annotation.JsonInclude;
    import com.fasterxml.jackson.annotation.JsonProperty;
    import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "requested_amount",
    "interest_rate",
    "duration",
    "total_repayment_value",
    "monthly_repayment_value"
})
@Generated("jsonschema2pojo")
public class CalculateRepaymentResponseDTO {

    @JsonProperty("requested_amount")
    private Double requestedAmount;
    @JsonProperty("interest_rate")
    private Integer interestRate;
    @JsonProperty("duration")
    private Integer duration;
    @JsonProperty("total_repayment_value")
    private Double totalRepaymentValue;
    @JsonProperty("monthly_repayment_value")
    private Double monthlyRepaymentValue;

    @JsonProperty("requested_amount")
    public Double getRequestedAmount() {
        return requestedAmount;
    }

    @JsonProperty("requested_amount")
    public void setRequestedAmount(Double requestedAmount) {
        this.requestedAmount = requestedAmount;
    }

    @JsonProperty("interest_rate")
    public Integer getInterestRate() {
        return interestRate;
    }

    @JsonProperty("interest_rate")
    public void setInterestRate(Integer interestRate) {
        this.interestRate = interestRate;
    }

    @JsonProperty("duration")
    public Integer getDuration() {
        return duration;
    }

    @JsonProperty("duration")
    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    @JsonProperty("total_repayment_value")
    public Double getTotalRepaymentValue() {
        return totalRepaymentValue;
    }

    @JsonProperty("total_repayment_value")
    public void setTotalRepaymentValue(Double totalRepaymentValue) {
        this.totalRepaymentValue = totalRepaymentValue;
    }

    @JsonProperty("monthly_repayment_value")
    public Double getMonthlyRepaymentValue() {
        return monthlyRepaymentValue;
    }

    @JsonProperty("monthly_repayment_value")
    public void setMonthlyRepaymentValue(Double monthlyRepaymentValue) {
        this.monthlyRepaymentValue = monthlyRepaymentValue;
    }

    @Override
    public String toString() {
        return "CalculateRepaymentResponseMockDTO{" +
            "requestedAmount=" + requestedAmount +
            ", interestRate=" + interestRate +
            ", duration=" + duration +
            ", totalRepaymentValue=" + totalRepaymentValue +
            ", monthlyRepaymentValue=" + monthlyRepaymentValue +
            '}';
    }
}

