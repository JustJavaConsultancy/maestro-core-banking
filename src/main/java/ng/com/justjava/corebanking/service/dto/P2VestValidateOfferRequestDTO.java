package ng.com.justjava.corebanking.service.dto;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "net_pay",
    "phone_number"
})
@Generated("jsonschema2pojo")
public class P2VestValidateOfferRequestDTO {

    @JsonProperty("net_pay")
    private Integer net_pay;
    @JsonProperty("phone_number")
    private String phone_number;

    @JsonProperty("net_pay")
    public Integer getNet_pay() {
        return net_pay;
    }

    @JsonProperty("net_pay")
    public void setNet_pay(Integer net_pay) {
        this.net_pay = net_pay;
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
        return "P2VestValidateOfferRequestDTO{" +
            "net_pay=" + net_pay +
            ", phone_number='" + phone_number + '\'' +
            '}';
    }
}
