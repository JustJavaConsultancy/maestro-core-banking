package ng.com.justjava.corebanking.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "Reference",
    "Amount",
    "TransactionID",
    "TraceID"
})
public class InvokeReferenceResponseDetailsDTO {
    @JsonProperty("Reference")
    private String reference;
    @JsonProperty("Amount")
    private String amount;
    @JsonProperty("TransactionID")
    private String transactionID;
    @JsonProperty("TraceID")
    private String traceID;

    @JsonProperty("Reference")
    public String getReference() {
        return reference;
    }

    @JsonProperty("Reference")
    public void setReference(String reference) {
        this.reference = reference;
    }

    @JsonProperty("Amount")
    public String getAmount() {
        return amount;
    }

    @JsonProperty("Amount")
    public void setAmount(String amount) {
        this.amount = amount;
    }

    @JsonProperty("TransactionID")
    public String getTransactionID() {
        return transactionID;
    }

    @JsonProperty("TransactionID")
    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    @JsonProperty("TraceID")
    public String getTraceID() {
        return traceID;
    }

    @JsonProperty("TraceID")
    public void setTraceID(String traceID) {
        this.traceID = traceID;
    }

    @Override
    public String toString() {
        return "InvokeReferenceResponseDetailsDTO{" +
            "reference='" + reference + '\'' +
            ", amount='" + amount + '\'' +
            ", transactionID='" + transactionID + '\'' +
            ", traceID='" + traceID + '\'' +
            '}';
    }
}
