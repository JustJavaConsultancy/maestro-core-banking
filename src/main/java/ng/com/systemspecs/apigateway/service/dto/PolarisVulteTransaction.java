package ng.com.systemspecs.apigateway.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "amount",
    "transaction_ref",
    "transaction_desc",
    "transaction_ref-parent",
    "customer",
    "meta",
    "details"
})
public class PolarisVulteTransaction {

    @JsonProperty("mock_mode")
    private String mockMode;
    @JsonProperty("amount")
    private BigDecimal amount;
    @JsonProperty("transaction_ref")
    private String transactionRef;
    @JsonProperty("transaction_desc")
    private String transactionDesc;
    @JsonProperty("transaction_ref-parent")
    private String transactionRefParent;
    @JsonProperty("customer")
    private PolarisVulteCustomer customer;
    @JsonProperty("meta")
    private PolarisVulteMeta meta;
    @JsonProperty("details")
    private Object details;

    @JsonProperty("amount")
    public BigDecimal getAmount() {
        return amount;
    }

    @JsonProperty("amount")
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @JsonProperty("transaction_ref")
    public String getTransactionRef() {
        return transactionRef;
    }

    @JsonProperty("transaction_ref")
    public void setTransactionRef(String transactionRef) {
        this.transactionRef = transactionRef;
    }

    @JsonProperty("transaction_desc")
    public String getTransactionDesc() {
        return transactionDesc;
    }

    @JsonProperty("transaction_desc")
    public void setTransactionDesc(String transactionDesc) {
        this.transactionDesc = transactionDesc;
    }

    @JsonProperty("transaction_ref-parent")
    public String getTransactionRefParent() {
        return transactionRefParent;
    }

    @JsonProperty("transaction_ref-parent")
    public void setTransactionRefParent(String transactionRefParent) {
        this.transactionRefParent = transactionRefParent;
    }

    @JsonProperty("customer")
    public PolarisVulteCustomer getCustomer() {
        return customer;
    }

    @JsonProperty("customer")
    public void setCustomer(PolarisVulteCustomer customer) {
        this.customer = customer;
    }

    @JsonProperty("meta")
    public PolarisVulteMeta getMeta() {
        return meta;
    }

    @JsonProperty("meta")
    public void setMeta(PolarisVulteMeta meta) {
        this.meta = meta;
    }

    @JsonProperty("details")
    public Object getDetails() {
        return details;
    }

    @JsonProperty("details")
    public void setDetails(Object details) {
        this.details = details;
    }

    public String getMockMode() {
        return mockMode;
    }

    public void setMockMode(String mockMode) {
        this.mockMode = mockMode;
    }

    @Override
    public String toString() {
        return "PolarisVulteTransaction{" +
            "amount=" + amount +
            ", transactionRef='" + transactionRef + '\'' +
            ", transactionDesc='" + transactionDesc + '\'' +
            ", transactionRefParent='" + transactionRefParent + '\'' +
            ", customer=" + customer +
            ", meta=" + meta +
            ", details=" + details +
            ", mockMode=" + mockMode +
            '}';
    }
}
