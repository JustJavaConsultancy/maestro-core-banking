package ng.com.justjava.corebanking.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "MerchantId",
    "TerminalId",
    "Amount",
    "Reference",
    "TransactionID"
})
public class ReversalDetailsDTO {

    @JsonProperty("MerchantId")
    private String merchantId;
    @JsonProperty("TerminalId")
    private String terminalId;
    @JsonProperty("Amount")
    private Double amount;
    @JsonProperty("Reference")
    private String reference;
    @JsonProperty("TransactionID")
    private String transactionID;

    @JsonProperty("MerchantId")
    public String getMerchantId() {
        return merchantId;
    }

    @JsonProperty("MerchantId")
    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    @JsonProperty("TerminalId")
    public String getTerminalId() {
        return terminalId;
    }

    @JsonProperty("TerminalId")
    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    @JsonProperty("Amount")
    public Double getAmount() {
        return amount;
    }

    @JsonProperty("Amount")
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @JsonProperty("Reference")
    public String getReference() {
        return reference;
    }

    @JsonProperty("Reference")
    public void setReference(String reference) {
        this.reference = reference;
    }

    @JsonProperty("TransactionID")
    public String getTransactionID() {
        return transactionID;
    }

    @JsonProperty("TransactionID")
    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }
}
