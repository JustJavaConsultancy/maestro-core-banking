package ng.com.justjava.corebanking.service.dto; ;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "error",
    "message",
    "amount",
    "ref",
    "date",
    "transactionID",
    "responseCode",
    "reference",
    "sequence",
    "clientReference"
})
@Generated("jsonschema2pojo")
public class PurchaseVTUResponseData {

    @JsonProperty("error")
    private Boolean error;
    @JsonProperty("message")
    private String message;
    @JsonProperty("amount")
    private String amount;
    @JsonProperty("ref")
    private String ref;
    @JsonProperty("date")
    private String date;
    @JsonProperty("transactionID")
    private String transactionID;
    @JsonProperty("responseCode")
    private String responseCode;
    @JsonProperty("reference")
    private String reference;
    @JsonProperty("sequence")
    private String sequence;
    @JsonProperty("clientReference")
    private String clientReference;

    @JsonProperty("error")
    public Boolean getError() {
        return error;
    }

    @JsonProperty("error")
    public void setError(Boolean error) {
        this.error = error;
    }

    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    @JsonProperty("message")
    public void setMessage(String message) {
        this.message = message;
    }

    @JsonProperty("amount")
    public String getAmount() {
        return amount;
    }

    @JsonProperty("amount")
    public void setAmount(String amount) {
        this.amount = amount;
    }

    @JsonProperty("ref")
    public String getRef() {
        return ref;
    }

    @JsonProperty("ref")
    public void setRef(String ref) {
        this.ref = ref;
    }

    @JsonProperty("date")
    public String getDate() {
        return date;
    }

    @JsonProperty("date")
    public void setDate(String date) {
        this.date = date;
    }

    @JsonProperty("transactionID")
    public String getTransactionID() {
        return transactionID;
    }

    @JsonProperty("transactionID")
    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    @JsonProperty("responseCode")
    public String getResponseCode() {
        return responseCode;
    }

    @JsonProperty("responseCode")
    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    @JsonProperty("reference")
    public String getReference() {
        return reference;
    }

    @JsonProperty("reference")
    public void setReference(String reference) {
        this.reference = reference;
    }

    @JsonProperty("sequence")
    public String getSequence() {
        return sequence;
    }

    @JsonProperty("sequence")
    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    @JsonProperty("clientReference")
    public String getClientReference() {
        return clientReference;
    }

    @JsonProperty("clientReference")
    public void setClientReference(String clientReference) {
        this.clientReference = clientReference;
    }

    @Override
    public String toString() {
        return "PurchaseVTUResponseData{" +
            "error=" + error +
            ", message='" + message + '\'' +
            ", amount='" + amount + '\'' +
            ", ref='" + ref + '\'' +
            ", date='" + date + '\'' +
            ", transactionID='" + transactionID + '\'' +
            ", responseCode='" + responseCode + '\'' +
            ", reference='" + reference + '\'' +
            ", sequence='" + sequence + '\'' +
            ", clientReference='" + clientReference + '\'' +
            '}';
    }
}
