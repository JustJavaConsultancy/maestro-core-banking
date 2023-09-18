package ng.com.systemspecs.apigateway.service.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "billPaymentProductId",
    "amount",
    "transactionRef",
    "name",
    "email",
    "phoneNumber",
    "customerId",
    "metadata"
})
@Generated("jsonschema2pojo")
public class InitiateBillerTransactionDTO implements Serializable {

    @JsonProperty("billPaymentProductId")
    private String billPaymentProductId;
    @JsonProperty("amount")
    private Integer amount;
    @JsonProperty("transactionRef")
    private String transactionRef;
    @JsonProperty("name")
    private String name;
    @JsonProperty("email")
    private String email;
    @JsonProperty("phoneNumber")
    private String phoneNumber;
    @JsonProperty("customerId")
    private String customerId;
    @JsonProperty("metadata")
    private BillerMetaData metadata;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    private String transactionPin;
    private String narration;
    private boolean redeemBonus;
    private Double bonusAmount;
    private String accountNumber;
    private String rrr;

    @JsonProperty("paymentFrequency")
    private String paymentFrequency;
    @JsonProperty("numberOfTimes")
    private Integer numberOfTimes;
    @JsonProperty("startDate")
    private String startDate;
    @JsonProperty("endDate")
    private String endDate;
    @JsonProperty("paymentType")
    private String paymentType;

    @JsonProperty("billPaymentProductId")
    public String getBillPaymentProductId() {
        return billPaymentProductId;
    }

    @JsonProperty("billPaymentProductId")
    public void setBillPaymentProductId(String billPaymentProductId) {
        this.billPaymentProductId = billPaymentProductId;
    }

    @JsonProperty("amount")
    public Integer getAmount() {
        return amount;
    }

    @JsonProperty("amount")
    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    @JsonProperty("transactionRef")
    public String getTransactionRef() {
        return transactionRef;
    }

    @JsonProperty("transactionRef")
    public void setTransactionRef(String transactionRef) {
        this.transactionRef = transactionRef;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    @JsonProperty("email")
    public void setEmail(String  email) {
        this.email = email;
    }

    @JsonProperty("phoneNumber")
    public String  getPhoneNumber() {
        return phoneNumber;
    }

    @JsonProperty("phoneNumber")
    public void setPhoneNumber(String  phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @JsonProperty("customerId")
    public String getCustomerId() {
        return customerId;
    }

    @JsonProperty("customerId")
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    @JsonProperty("metadata")
    public BillerMetaData getMetadata() {
        return metadata;
    }

    @JsonProperty("metadata")
    public void setMetadata(BillerMetaData metadata) {
        this.metadata = metadata;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public void setAdditionalProperties(Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
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

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getRrr() {
        return rrr;
    }

    public void setRrr(String rrr) {
        this.rrr = rrr;
    }

    public String getPaymentFrequency() {
        return paymentFrequency;
    }

    public void setPaymentFrequency(String paymentFrequency) {
        this.paymentFrequency = paymentFrequency;
    }

    public Integer getNumberOfTimes() {
        return numberOfTimes;
    }

    public void setNumberOfTimes(Integer numberOfTimes) {
        this.numberOfTimes = numberOfTimes;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    @Override
    public String toString() {
        return "InitiateBillerTransactionDTO{" +
            "billPaymentProductId='" + billPaymentProductId + '\'' +
            ", amount=" + amount +
            ", transactionRef='" + transactionRef + '\'' +
            ", name='" + name + '\'' +
            ", email='" + email + '\'' +
            ", phoneNumber='" + phoneNumber + '\'' +
            ", customerId='" + customerId + '\'' +
            ", metadata=" + metadata +
            ", additionalProperties=" + additionalProperties +
            ", transactionPin='" + transactionPin + '\'' +
            ", narration='" + narration + '\'' +
            ", redeemBonus=" + redeemBonus +
            ", bonusAmount=" + bonusAmount +
            ", accountNumber='" + accountNumber + '\'' +
            ", rrr='" + rrr + '\'' +
            ", paymentFrequency='" + paymentFrequency + '\'' +
            ", numberOfTimes=" + numberOfTimes +
            ", startDate='" + startDate + '\'' +
            ", endDate='" + endDate + '\'' +
            ", paymentType='" + paymentType + '\'' +
            '}';
    }
}
