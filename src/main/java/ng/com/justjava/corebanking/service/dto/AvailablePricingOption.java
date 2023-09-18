package ng.com.justjava.corebanking.service.dto;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "monthsPaidFor",
    "price",
    "invoicePeriod",
    "totalAmount"
})
@Generated("jsonschema2pojo")
public class AvailablePricingOption {

    @JsonProperty("monthsPaidFor")
    private Double monthsPaidFor;
    @JsonProperty("price")
    private Double price;
    @JsonProperty("invoicePeriod")
    private Double invoicePeriod;
    @JsonProperty("totalAmount")
    private Double totalAmount;

    @JsonProperty("monthsPaidFor")
    public Double getMonthsPaidFor() {
        return monthsPaidFor;
    }

    @JsonProperty("monthsPaidFor")
    public void setMonthsPaidFor(Double monthsPaidFor) {
        this.monthsPaidFor = monthsPaidFor;
    }

    @JsonProperty("price")
    public Double getPrice() {
        return price;
    }

    @JsonProperty("price")
    public void setPrice(Double price) {
        this.price = price;
    }

    @JsonProperty("invoicePeriod")
    public Double getInvoicePeriod() {
        return invoicePeriod;
    }

    @JsonProperty("invoicePeriod")
    public void setInvoicePeriod(Double invoicePeriod) {
        this.invoicePeriod = invoicePeriod;
    }

    @JsonProperty("totalAmount")
    public Double getTotalAmount() {
        return totalAmount;
    }

    @JsonProperty("totalAmount")
    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    @Override
    public String toString() {
        return "AvailablePricingOption{" +
            "monthsPaidFor=" + monthsPaidFor +
            ", price=" + price +
            ", invoicePeriod=" + invoicePeriod +
            ", totalAmount=" + totalAmount +
            '}';
    }
}
