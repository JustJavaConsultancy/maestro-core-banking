package ng.com.systemspecs.apigateway.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "phone",
    "code",
    "renew",
    "paymentMethod",
    "service",
    "clientReference",
    "pin",
    "productMonths",
    "totalAmount",
    "productCode",
    "card",
    "channel"
})
@Generated("jsonschema2pojo")
public class MultiChoiceSubscriptionRequestDTO {

    @JsonProperty("phone")
    private String phone;
    @JsonProperty("code")
    private String code;
    @JsonProperty("renew")
    private boolean renew;
    @JsonProperty("paymentMethod")
    private String paymentMethod;
    @JsonProperty("service")
    private String service;
    @JsonProperty("clientReference")
    private String clientReference;
    @JsonProperty("pin")
    private String pin = "c849ebd417ee86e19f46f5aa09f769c8d5fc6fa07aa319e1e7ad5b396a8b6e4e";
    @JsonProperty("productMonths")
    private Integer productMonths;
    @JsonProperty("totalAmount")
    private String totalAmount;
    @JsonProperty("productCode")
    private String productCode;
    @JsonProperty("card")
    private ElectricityCard card;
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

    @JsonProperty("code")
    public String getCode() {
        return code;
    }

    @JsonProperty("code")
    public void setCode(String code) {
        this.code = code;
    }

    @JsonProperty("renew")
    public boolean getRenew() {
        return renew;
    }

    @JsonProperty("renew")
    public void setRenew(boolean renew) {
        this.renew = renew;
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

    @JsonProperty("productMonths")
    public Integer getProductMonths() {
        return productMonths;
    }

    @JsonProperty("productMonths")
    public void setProductMonths(Integer productMonths) {
        this.productMonths = productMonths;
    }

    @JsonProperty("totalAmount")
    public String getTotalAmount() {
        return totalAmount;
    }

    @JsonProperty("totalAmount")
    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
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
        return bonusAmount;
    }

    public void setBonusAmount(Double bonusAmount) {
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
        return "MultiChoiceSubscriptionRequestDTO{" +
            "phone='" + phone + '\'' +
            ", code='" + code + '\'' +
            ", renew=" + renew +
            ", paymentMethod='" + paymentMethod + '\'' +
            ", service='" + service + '\'' +
            ", clientReference='" + clientReference + '\'' +
            ", pin='" + pin + '\'' +
            ", productMonths=" + productMonths +
            ", totalAmount='" + totalAmount + '\'' +
            ", productCode='" + productCode + '\'' +
            ", card=" + card +
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
