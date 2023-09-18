package ng.com.systemspecs.apigateway.service.dto;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "clientNo",
    "amount",
    "paymentType",
    "paymentMethod"
})
@Generated("jsonschema2pojo")
public class MigoPostPaymentsRequestDTO {

    @JsonProperty("clientNo")
    private String clientNo;
    @JsonProperty("amount")
    private Integer amount;
    @JsonProperty("paymentType")
    private String paymentType;
    @JsonProperty("paymentMethod")
    private MigoPaymentMethodDTO migoPaymentMethod;

    @JsonProperty("clientNo")
    public String getClientNo() {
        return clientNo;
    }

    @JsonProperty("clientNo")
    public void setClientNo(String clientNo) {
        this.clientNo = clientNo;
    }

    @JsonProperty("amount")
    public Integer getAmount() {
        return amount;
    }

    @JsonProperty("amount")
    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    @JsonProperty("paymentType")
    public String getPaymentType() {
        return paymentType;
    }

    @JsonProperty("paymentType")
    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    @JsonProperty("paymentMethod")
    public MigoPaymentMethodDTO getMigoPaymentMethod() {
        return migoPaymentMethod;
    }

    @JsonProperty("paymentMethod")
    public void setMigoPaymentMethod(MigoPaymentMethodDTO migoPaymentMethod) {
        this.migoPaymentMethod = migoPaymentMethod;
    }

    @Override
    public String toString() {
        return "MigoPostPaymentsRequestDTO{" +
            "clientNo='" + clientNo + '\'' +
            ", amount=" + amount +
            ", paymentType='" + paymentType + '\'' +
            ", migoPaymentMethod=" + migoPaymentMethod +
            '}';
    }
}
