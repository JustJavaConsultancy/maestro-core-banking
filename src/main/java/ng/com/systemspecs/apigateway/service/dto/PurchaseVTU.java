package ng.com.systemspecs.apigateway.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "phone",
    "paymentMethod",
    "service",
    "amount",
    "pin",
    "clientReference",
    "channel"
})
@Generated("jsonschema2pojo")
public class PurchaseVTU {

    @JsonProperty("phone")
    private String phone;
    @JsonProperty("paymentMethod")
    private String paymentMethod;
    @JsonProperty("service")
    private String service;
    @JsonProperty("amount")
    private String amount;
    @JsonProperty("pin")
    private String pin = "c849ebd417ee86e19f46f5aa09f769c8d5fc6fa07aa319e1e7ad5b396a8b6e4e";
    @JsonProperty("clientReference")
    private String clientReference;
    @JsonProperty("channel")
    private String channel;

    private String sourceAccountNumber;
    private String transactionPin;
    private String narration;
    private boolean redeemBonus;
    private Double bonusAmount;
    private Double charges = 0.0;


    @JsonProperty("phone")
    public String getPhone() {
        return phone;
    }

    @JsonProperty("phone")
    public void setPhone(String phone) {
        this.phone = phone;
    }

    @JsonProperty("paymentMethod")
    public String getPaymentMethod() {
        return paymentMethod;
    }

    @JsonProperty("paymentMethod")
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    @JsonProperty("service")
    public String getService() {
        return service;
    }

    @JsonProperty("service")
    public void setService(String service) {
        this.service = service;
    }

    @JsonProperty("amount")
    public String getAmount() {
        return amount;
    }

    @JsonProperty("amount")
    public void setAmount(String amount) {
        this.amount = amount;
    }

    @JsonProperty("pin")
    public String getPin() {
        return pin;
    }

    @JsonProperty("pin")
    public void setPin(String pin) {
        this.pin = pin;
    }

    @JsonProperty("clientReference")
    public String getClientReference() {
        return clientReference;
    }

    @JsonProperty("clientReference")
    public void setClientReference(String clientReference) {
        this.clientReference = clientReference;
    }

    @JsonProperty("channel")
    public String getChannel() {
        return channel;
    }

    @JsonProperty("channel")
    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getSourceAccountNumber() {
        return sourceAccountNumber;
    }

    public void setSourceAccountNumber(String sourceAccountNumber) {
        this.sourceAccountNumber = sourceAccountNumber;
    }

    public String getTransactionPin() {
        return transactionPin;
    }

    public void setTransactionPin(String transactionPin) {
        this.transactionPin = transactionPin;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public boolean isRedeemBonus() {
        return redeemBonus;
    }

    public void setRedeemBonus(boolean redeemBonus) {
        this.redeemBonus = redeemBonus;
    }

    public Double getBonusAmount() {
        if (bonusAmount == null) {
            return 0.0;
        }
        return bonusAmount;
    }

    public void setBonusAmount(Double bonusAmount) {
        if (bonusAmount == null) {
            bonusAmount = 0.0;
        }
        this.bonusAmount = bonusAmount;
    }

    public Double getCharges() {
        if (charges == null){
            return 0.0;
        }
        return charges;
    }

    public void setCharges(Double charges) {
        if (charges == null){
            charges = 0.00;
        }
        this.charges = charges;
    }

    @Override
    public String toString() {
        return "PurchaseVTU{" +
            "phone='" + phone + '\'' +
            ", paymentMethod='" + paymentMethod + '\'' +
            ", service='" + service + '\'' +
            ", amount='" + amount + '\'' +
            ", pin='" + pin + '\'' +
            ", clientReference='" + clientReference + '\'' +
            ", channel='" + channel + '\'' +
            ", sourceAccountNumber='" + sourceAccountNumber + '\'' +
            ", transactionPin='" + transactionPin + '\'' +
            ", narration='" + narration + '\'' +
            ", redeemBonus=" + redeemBonus +
            ", bonusAmount=" + bonusAmount +
            ", charges=" + charges +
            '}';
    }
}
