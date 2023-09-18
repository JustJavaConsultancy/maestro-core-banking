package ng.com.systemspecs.apigateway.service.dto;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "loan_amount",
    "tenor",
    "phone_number"
})
@Generated("jsonschema2pojo")
public class P2VestOfferRequestDTO {

    @JsonProperty("loan_amount")
    private Integer loan_amount;
    @JsonProperty("tenor")
    private Integer tenor;
    @JsonProperty("phone_number")
    private String phone_number;

    @JsonProperty("loan_amount")
    public Integer getLoan_amount() {
        return loan_amount;
    }

    @JsonProperty("loan_amount")
    public void setLoan_amount(Integer loan_amount) {
        this.loan_amount = loan_amount;
    }

    @JsonProperty("tenor")
    public Integer getTenor() {
        return tenor;
    }

    @JsonProperty("tenor")
    public void setTenor(Integer tenor) {
        this.tenor = tenor;
    }

    @JsonProperty("phone_number")
    public String getPhone_number() {
        return phone_number;
    }

    @JsonProperty("phone_number")
    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    @Override
    public String toString() {
        return "P2VestOfferRequestDTO{" +
            "loan_amount=" + loan_amount +
            ", tenor=" + tenor +
            ", phone_number='" + phone_number + '\'' +
            '}';
    }
}
