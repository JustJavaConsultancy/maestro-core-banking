package ng.com.justjava.corebanking.service.dto;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "offer_okay",
    "phone",
    "duration",
    "interest",
    "amount"
})
@Generated("jsonschema2pojo")
public class RequestLoanOfferResponseMockDTO {

    @JsonProperty("offer_okay")
    private Boolean offerOkay;
    @JsonProperty("phone")
    private String phone;
    @JsonProperty("duration")
    private Integer duration;
    @JsonProperty("interest")
    private Integer interest;
    @JsonProperty("amount")
    private Integer amount;

    @JsonProperty("offer_okay")
    public Boolean getOfferOkay() {
        return offerOkay;
    }

    @JsonProperty("offer_okay")
    public void setOfferOkay(Boolean offerOkay) {
        this.offerOkay = offerOkay;
    }

    @JsonProperty("phone")
    public String getPhone() {
        return phone;
    }

    @JsonProperty("phone")
    public void setPhone(String phone) {
        this.phone = phone;
    }

    @JsonProperty("duration")
    public Integer getDuration() {
        return duration;
    }

    @JsonProperty("duration")
    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    @JsonProperty("interest")
    public Integer getInterest() {
        return interest;
    }

    @JsonProperty("interest")
    public void setInterest(Integer interest) {
        this.interest = interest;
    }

    @JsonProperty("amount")
    public Integer getAmount() {
        return amount;
    }

    @JsonProperty("amount")
    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "RequestLoanOfferResponseMockDTO{" +
            "offerOkay=" + offerOkay +
            ", phone='" + phone + '\'' +
            ", duration=" + duration +
            ", interest=" + interest +
            ", amount=" + amount +
            '}';
    }
}
