package ng.com.systemspecs.apigateway.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "sourceAccount",
    "destinationAccount",
    "destinationAccountName",
    "narration",
    "TransactionTrackingRef",
    "amount"
})
@Generated("jsonschema2pojo")
public class FundIntraDTO {

    @JsonProperty("sourceAccount")
    private String sourceAccount;
    @JsonProperty("destinationBankCode")
    private String destinationBankCode;
    @JsonProperty("destinationAccount")
    private String destinationAccount;
    @JsonProperty("destinationAccountName")
    private String destinationAccountName;
    @JsonProperty("narration")
    private String narration;
    @JsonProperty("TransactionTrackingRef")
    private String transactionTrackingRef;
    @JsonProperty("amount")
    private String amount;

    @JsonProperty("sourceAccount")
    public String getSourceAccount() {
        return sourceAccount;
    }

    @JsonProperty("sourceAccount")
    public void setSourceAccount(String sourceAccount) {
        this.sourceAccount = sourceAccount;
    }

    public String getDestinationBankCode() {
        return destinationBankCode;
    }

    public void setDestinationBankCode(String destinationBankCode) {
        this.destinationBankCode = destinationBankCode;
    }

    @JsonProperty("destinationAccount")
    public String getDestinationAccount() {
        return destinationAccount;
    }

    @JsonProperty("destinationAccount")
    public void setDestinationAccount(String destinationAccount) {
        this.destinationAccount = destinationAccount;
    }

    @JsonProperty("destinationAccountName")
    public String getDestinationAccountName() {
        return destinationAccountName;
    }

    @JsonProperty("destinationAccountName")
    public void setDestinationAccountName(String destinationAccountName) {
        this.destinationAccountName = destinationAccountName;
    }

    @JsonProperty("narration")
    public String getNarration() {
        return narration;
    }

    @JsonProperty("narration")
    public void setNarration(String narration) {
        this.narration = narration;
    }

    @JsonProperty("TransactionTrackingRef")
    public String getTransactionTrackingRef() {
        return transactionTrackingRef;
    }

    @JsonProperty("TransactionTrackingRef")
    public void setTransactionTrackingRef(String transactionTrackingRef) {
        this.transactionTrackingRef = transactionTrackingRef;
    }

    @JsonProperty("amount")
    public String getAmount() {
        return amount;
    }

    @JsonProperty("amount")
    public void setAmount(String amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "FundIntraDTO{" +
            "sourceAccount='" + sourceAccount + '\'' +
            ", destinationAccount='" + destinationAccount + '\'' +
            ", destinationAccountName='" + destinationAccountName + '\'' +
            ", narration='" + narration + '\'' +
            ", transactionTrackingRef='" + transactionTrackingRef + '\'' +
            ", amount='" + amount + '\'' +
            '}';
    }
}
