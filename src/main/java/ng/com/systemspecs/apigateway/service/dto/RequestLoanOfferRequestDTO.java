package ng.com.systemspecs.apigateway.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "phoneNumber",
    "duration",
    "requested_amount"
})
@Generated("jsonschema2pojo")
public class RequestLoanOfferRequestDTO {

    @JsonProperty("phoneNumber")
    private String phoneNumber;
    @JsonProperty("duration")
    private Double duration;
    @JsonProperty("requested_amount")
    private Double requestedAmount;

    @JsonProperty("phoneNumber")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @JsonProperty("phoneNumber")
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @JsonProperty("duration")
    public Double getDuration() {
        return duration;
    }

    @JsonProperty("duration")
    public void setDuration(Double duration) {
        this.duration = duration;
    }

    @JsonProperty("requested_amount")
    public Double getRequestedAmount() {
        return requestedAmount;
    }

    @JsonProperty("requested_amount")
    public void setRequestedAmount(Double requestedAmount) {
        this.requestedAmount = requestedAmount;
    }

    @Override
    public String toString() {
        return "RequestLoanOfferRequestDTO{" +
            "phoneNumber='" + phoneNumber + '\'' +
            ", duration=" + duration +
            ", requested_amount=" + requestedAmount +
            '}';
    }
}
