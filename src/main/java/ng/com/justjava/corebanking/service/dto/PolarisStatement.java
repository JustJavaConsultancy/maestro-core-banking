package ng.com.justjava.corebanking.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "transaction_reference",
    "transaction_amount",
    "balance",
    "transaction_type",
    "transaction_date",
    "description"
})
@Generated("jsonschema2pojo")
public class PolarisStatement {

    @JsonProperty("transaction_reference")
    private String transactionReference;
    @JsonProperty("transaction_amount")
    private Double transactionAmount;
    @JsonProperty("balance")
    private Double balance;
    @JsonProperty("transaction_type")
    private String transactionType;
    @JsonProperty("transaction_date")
    private String transactionDate;
    @JsonProperty("description")
    private String description;

    @JsonProperty("transaction_reference")
    public String getTransactionReference() {
        return transactionReference;
    }

    @JsonProperty("transaction_reference")
    public void setTransactionReference(String transactionReference) {
        this.transactionReference = transactionReference;
    }

    @JsonProperty("transaction_amount")
    public Double getTransactionAmount() {
        return transactionAmount;
    }

    @JsonProperty("transaction_amount")
    public void setTransactionAmount(Double transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    @JsonProperty("balance")
    public Double getBalance() {
        return balance;
    }

    @JsonProperty("balance")
    public void setBalance(Double balance) {
        this.balance = balance;
    }

    @JsonProperty("transaction_type")
    public String getTransactionType() {
        return transactionType;
    }

    @JsonProperty("transaction_type")
    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    @JsonProperty("transaction_date")
    public String getTransactionDate() {
        return transactionDate;
    }

    @JsonProperty("transaction_date")
    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "PolarisStatement{" +
            "transactionReference='" + transactionReference + '\'' +
            ", transactionAmount=" + transactionAmount +
            ", balance=" + balance +
            ", transactionType='" + transactionType + '\'' +
            ", transactionDate='" + transactionDate + '\'' +
            ", description='" + description + '\'' +
            '}';
    }
}
