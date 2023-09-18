package ng.com.systemspecs.apigateway.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "amount_eligible"
})
@Generated("jsonschema2pojo")
public class P2VestValidateOfferResponseDataDTO {

    @JsonProperty("amount_eligible")
    private Double amount_eligible;

    @JsonProperty("amount_eligible")
    public Double getAmount_eligible() {
        return amount_eligible;
    }

    @JsonProperty("amount_eligible")
    public void setAmount_eligible(Double amount_eligible) {
        this.amount_eligible = amount_eligible;
    }

    @Override
    public String toString() {
        return "P2VestValidateOfferResponseDataDTO{" +
            "amount_eligible=" + amount_eligible +
            '}';
    }
}
