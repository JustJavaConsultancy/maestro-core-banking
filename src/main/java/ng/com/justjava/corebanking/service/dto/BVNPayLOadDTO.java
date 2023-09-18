package ng.com.justjava.corebanking.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({
    "TransactionTrackingRef",
    "bvn"
})

public class BVNPayLOadDTO {

    @JsonProperty("bvn")
    private String bvn;
    @JsonProperty("TransactionTrackingRef")
    private String TransactionTrackingRef;


    public String getBvn() {
        return bvn;
    }

    public void setBvn(String bvn) {
        this.bvn = bvn;
    }

    @JsonProperty("TransactionTrackingRef")
    public String getTransactionTrackingRef() {
        return TransactionTrackingRef;
    }

    @JsonProperty("TransactionTrackingRef")
    public void setTransactionTrackingRef(String transactionTrackingRef) {
        TransactionTrackingRef = transactionTrackingRef;
    }

    @Override
    public String toString() {
        return "BVNPayLOadDTO{" +
            "bvnNumber='" + bvn + '\'' +
            "TransactionTrackingRef" + TransactionTrackingRef + '\'' +
            '}';
    }
}
