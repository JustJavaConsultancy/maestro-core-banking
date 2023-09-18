package ng.com.justjava.corebanking.service.dto;

import com.fasterxml.jackson.annotation.*;

import javax.annotation.Generated;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "TransactionTrackingRef",
    "product",
    "accountNumber",
    "PhoneNo",
    "Email",
    "bvn",
    "amount",
    "productNumber",
    "productProvider",
    "productTenure"
})
@Generated("jsonschema2pojo")
public class LoanOfferDTO {

    @JsonProperty("TransactionTrackingRef")
    private String transactionTrackingRef;
    @JsonProperty("product")
    private String product;
    @JsonProperty("accountNumber")
    private String accountNumber;
    @JsonProperty("PhoneNo")
    private String phoneNo;
    @JsonProperty("Email")
    private String email;
    @JsonProperty("bvn")
    private String bvn;
    @JsonProperty("amount")
    private String amount;
    @JsonProperty("productNumber")
    private String productNumber;
    @JsonProperty("productProvider")
    private String productProvider;
    @JsonProperty("productTenure")
    private String productTenure;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("TransactionTrackingRef")
    public String getTransactionTrackingRef() {
        return transactionTrackingRef;
    }

    @JsonProperty("TransactionTrackingRef")
    public void setTransactionTrackingRef(String transactionTrackingRef) {
        this.transactionTrackingRef = transactionTrackingRef;
    }

    @JsonProperty("product")
    public String getProduct() {
        return product;
    }

    @JsonProperty("product")
    public void setProduct(String product) {
        this.product = product;
    }

    @JsonProperty("accountNumber")
    public String getAccountNumber() {
        return accountNumber;
    }

    @JsonProperty("accountNumber")
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    @JsonProperty("PhoneNo")
    public String getPhoneNo() {
        return phoneNo;
    }

    @JsonProperty("PhoneNo")
    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    @JsonProperty("Email")
    public String getEmail() {
        return email;
    }

    @JsonProperty("Email")
    public void setEmail(String email) {
        this.email = email;
    }

    @JsonProperty("bvn")
    public String getBvn() {
        return bvn;
    }

    @JsonProperty("bvn")
    public void setBvn(String bvn) {
        this.bvn = bvn;
    }

    @JsonProperty("amount")
    public String getAmount() {
        return amount;
    }

    @JsonProperty("amount")
    public void setAmount(String amount) {
        this.amount = amount;
    }

    @JsonProperty("productNumber")
    public String getProductNumber() {
        return productNumber;
    }

    @JsonProperty("productNumber")
    public void setProductNumber(String productNumber) {
        this.productNumber = productNumber;
    }

    @JsonProperty("productProvider")
    public String getProductProvider() {
        return productProvider;
    }

    @JsonProperty("productProvider")
    public void setProductProvider(String productProvider) {
        this.productProvider = productProvider;
    }

    @JsonProperty("productTenure")
    public String getProductTenure() {
        return productTenure;
    }

    @JsonProperty("productTenure")
    public void setProductTenure(String productTenure) {
        this.productTenure = productTenure;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public String toString() {
        return "LoanOfferDTO{" +
            "transactionTrackingRef='" + transactionTrackingRef + '\'' +
            ", product='" + product + '\'' +
            ", accountNumber='" + accountNumber + '\'' +
            ", phoneNo='" + phoneNo + '\'' +
            ", email='" + email + '\'' +
            ", bvn='" + bvn + '\'' +
            ", amount='" + amount + '\'' +
            ", productNumber='" + productNumber + '\'' +
            ", productProvider='" + productProvider + '\'' +
            ", productTenure='" + productTenure + '\'' +
            ", additionalProperties=" + additionalProperties +
            '}';
    }
}
