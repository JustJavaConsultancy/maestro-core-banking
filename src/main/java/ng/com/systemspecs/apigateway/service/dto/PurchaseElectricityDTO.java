package ng.com.systemspecs.apigateway.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "customerPhoneNumber",
    "paymentMethod",
    "service",
    "clientReference",
    "pin",
    "productCode",
    "card"
})
@Generated("jsonschema2pojo")
public class PurchaseElectricityDTO {

    @JsonProperty("customerPhoneNumber")
    private String customerPhoneNumber;
    @JsonProperty("paymentMethod")
    private String paymentMethod;
    @JsonProperty("service")
    private String service;
    @JsonProperty("clientReference")
    private String clientReference;
    @JsonProperty("pin")
    private String pin = "c849ebd417ee86e19f46f5aa09f769c8d5fc6fa07aa319e1e7ad5b396a8b6e4e";
    @JsonProperty("productCode")
    private String productCode;
    @JsonProperty("card")
    private ElectricityCard card;
    @JsonProperty("amount")
    private Double amount;

    private String sourceAccountNumber;
    private String transactionPin;
    private String narration;
    private boolean redeemBonus;
    private Double bonusAmount;
    private Double charges = 0.0;

    @JsonProperty("customerPhoneNumber")
    public String getCustomerPhoneNumber() {
        return customerPhoneNumber;
    }

    @JsonProperty("customerPhoneNumber")
    public void setCustomerPhoneNumber(String customerPhoneNumber) {
        this.customerPhoneNumber = customerPhoneNumber;
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

    public boolean isRedeemBonus() {
        return redeemBonus;
    }

    public void setRedeemBonus(boolean redeemBonus) {
        this.redeemBonus = redeemBonus;
    }

    public Double getAmount() {
        if (amount == null) {
            return 0.0;
        }
        return amount;
    }

    public void setAmount(Double amount) {
        if (amount == null) {
            amount = 0.0;
        }
        this.amount = amount;
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
        return "PurchaseElectricityDTO{" +
            "customerPhoneNumber='" + customerPhoneNumber + '\'' +
            ", paymentMethod='" + paymentMethod + '\'' +
            ", service='" + service + '\'' +
            ", clientReference='" + clientReference + '\'' +
            ", pin='" + pin + '\'' +
            ", productCode='" + productCode + '\'' +
            ", card=" + card +
            ", amount=" + amount +
            ", sourceAccountNumber='" + sourceAccountNumber + '\'' +
            ", transactionPin='" + transactionPin + '\'' +
            ", narration='" + narration + '\'' +
            ", redeemBonus=" + redeemBonus +
            ", bonusAmount=" + bonusAmount +
            ", charges=" + charges +
            '}';
    }
}
