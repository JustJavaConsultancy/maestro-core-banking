package ng.com.justjava.corebanking.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "phone",
    "type",
    "code",
    "amount",
    "paymentMethod",
    "service",
    "clientReference",
    "pin",
    "productCode",
    "card"
})
@Generated("jsonschema2pojo")
public class InternetSubscriptionDTO {

    @JsonProperty("phone")
    private String phone;
    @JsonProperty("type")
    private String type;
    @JsonProperty("code")
    private String code;
    @JsonProperty("amount")
    private String amount;
    @JsonProperty("paymentMethod")
    private String paymentMethod;
    @JsonProperty("service")
    private String service;
    @JsonProperty("clientReference")
    private String clientReference;
    @JsonProperty("pin")
    private String pin;
    @JsonProperty("productCode")
    private String productCode;
    @JsonProperty("card")
    private ElectricityCard card;


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

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("code")
    public String getCode() {
        return code;
    }

    @JsonProperty("code")
    public void setCode(String code) {
        this.code = code;
    }

    @JsonProperty("amount")
    public String getAmount() {
        return amount;
    }

    @JsonProperty("amount")
    public void setAmount(String amount) {
        this.amount = amount;
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

    @JsonProperty("clientReference")
    public String getClientReference() {
        return clientReference;
    }

    @JsonProperty("clientReference")
    public void setClientReference(String clientReference) {
        this.clientReference = clientReference;
    }

    @JsonProperty("pin")
    public String getPin() {
        return pin;
    }

    @JsonProperty("pin")
    public void setPin(String pin) {
        this.pin = pin;
    }

    @JsonProperty("productCode")
    public String getProductCode() {
        return productCode;
    }

    @JsonProperty("productCode")
    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    @JsonProperty("card")
    public ElectricityCard getCard() {
        return card;
    }

    @JsonProperty("card")
    public void setCard(ElectricityCard card) {
        this.card = card;
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
        return charges;
    }

    public void setCharges(Double charges) {
        this.charges = charges;
    }

    @Override
    public String toString() {
        return "InternetSubscriptionDTO{" +
            "phone='" + phone + '\'' +
            ", type='" + type + '\'' +
            ", code='" + code + '\'' +
            ", amount='" + amount + '\'' +
            ", paymentMethod='" + paymentMethod + '\'' +
            ", service='" + service + '\'' +
            ", clientReference='" + clientReference + '\'' +
            ", pin='" + pin + '\'' +
            ", productCode='" + productCode + '\'' +
            ", card=" + card +
            ", sourceAccountNumber='" + sourceAccountNumber + '\'' +
            ", transactionPin='" + transactionPin + '\'' +
            ", narration='" + narration + '\'' +
            ", redeemBonus=" + redeemBonus +
            ", bonusAmount=" + bonusAmount +
            ", charges=" + charges +
            '}';
    }
}
