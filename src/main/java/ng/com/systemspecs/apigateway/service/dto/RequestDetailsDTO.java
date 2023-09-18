package ng.com.systemspecs.apigateway.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "TerminalId",
    "Amount",
    "MerchantId",
    "TransactionID"
})
public class RequestDetailsDTO {
    @JsonProperty("TerminalId")
    private String terminalId;
    @JsonProperty("Amount")
    private Double amount;
    @JsonProperty("MerchantId")
    private String merchantId;
    @JsonProperty("TransactionID")
    private String transactionID;

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

    @JsonProperty("MerchantId")
    public String getMerchantId() {
        return merchantId;
    }

    @JsonProperty("MerchantId")
    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    @JsonProperty("TransactionID")
    public String getTransactionID() {
        return transactionID;
    }

    @JsonProperty("TransactionID")
    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    @Override
    public String toString() {
        return "RequestDetailsDTO{" +
            "terminalId='" + terminalId + '\'' +
            ", amount=" + amount +
            ", merchantId='" + merchantId + '\'' +
            ", transactionID='" + transactionID + '\'' +
            '}';
    }
}
