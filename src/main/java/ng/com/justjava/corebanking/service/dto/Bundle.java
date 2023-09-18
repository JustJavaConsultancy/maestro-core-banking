package ng.com.justjava.corebanking.service.dto;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "name",
    "code",
    "displayPrice",
    "price",
    "fee",
    "validity"
})
@Generated("jsonschema2pojo")
public class Bundle {

    @JsonProperty("name")
    private String name;
    @JsonProperty("code")
    private String code;
    @JsonProperty("displayPrice")
    private Double displayPrice;
    @JsonProperty("price")
    private Double price;
    @JsonProperty("fee")
    private Double fee;
    @JsonProperty("validity")
    private String validity;

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("code")
    public String getCode() {
        return code;
    }

    @JsonProperty("code")
    public void setCode(String code) {
        this.code = code;
    }

    @JsonProperty("displayPrice")
    public Double getDisplayPrice() {
        return displayPrice;
    }

    @JsonProperty("displayPrice")
    public void setDisplayPrice(Double displayPrice) {
        this.displayPrice = displayPrice;
    }

    @JsonProperty("price")
    public Double getPrice() {
        return price;
    }

    @JsonProperty("price")
    public void setPrice(Double price) {
        this.price = price;
    }

    @JsonProperty("fee")
    public Double getFee() {
        return fee;
    }

    @JsonProperty("fee")
    public void setFee(Double fee) {
        this.fee = fee;
    }

    @JsonProperty("validity")
    public String getValidity() {
        return validity;
    }

    @JsonProperty("validity")
    public void setValidity(String validity) {
        this.validity = validity;
    }

    @Override
    public String toString() {
        return "Bundle{" +
            "name='" + name + '\'' +
            ", code='" + code + '\'' +
            ", displayPrice=" + displayPrice +
            ", price=" + price +
            ", fee=" + fee +
            ", validity='" + validity + '\'' +
            '}';
    }
}
