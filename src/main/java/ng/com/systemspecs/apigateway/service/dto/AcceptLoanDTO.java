package ng.com.systemspecs.apigateway.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "OfferId",
    "TransactionTrackingRef",
    "accountNumber"
})
@Generated("jsonschema2pojo")
public class AcceptLoanDTO {

    @JsonProperty("OfferId")
    private String offerId;
    @JsonProperty("TransactionTrackingRef")
    private String transactionTrackingRef;
    @JsonProperty("accountNumber")
    private String accountNumber;

    @JsonProperty("OfferId")
    public String getOfferId() {
        return offerId;
    }

    @JsonProperty("OfferId")
    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    @JsonProperty("TransactionTrackingRef")
    public String getTransactionTrackingRef() {
        return transactionTrackingRef;
    }

    @JsonProperty("TransactionTrackingRef")
    public void setTransactionTrackingRef(String transactionTrackingRef) {
        this.transactionTrackingRef = transactionTrackingRef;
    }

    @JsonProperty("accountNumber")
    public String getAccountNumber() {
        return accountNumber;
    }

    @JsonProperty("accountNumber")
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    @Override
    public String toString() {
        return "AcceptLoanDTO{" +
            "offerId='" + offerId + '\'' +
            ", transactionTrackingRef='" + transactionTrackingRef + '\'' +
            ", accountNumber='" + accountNumber + '\'' +
            '}';
    }
}
