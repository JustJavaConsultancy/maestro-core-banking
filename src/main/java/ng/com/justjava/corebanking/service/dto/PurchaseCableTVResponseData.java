package ng.com.justjava.corebanking.service.dto;

//public class  {
//}


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "error",
    "message",
    "ref",
    "amount",
    "reversal",
    "bouquetCode",
    "bouquetName",
    "account",
    "externalReference",
    "auditReferenceNumber",
    "date",
    "response",
    "responseCode",
    "reference",
    "sequence",
    "clientReference"
})
@Generated("jsonschema2pojo")
public class PurchaseCableTVResponseData {

    @JsonProperty("status")
    private String status;
    @JsonProperty("error")
    private Boolean error;
    @JsonProperty("message")
    private String message;
    @JsonProperty("ref")
    private String ref;
    @JsonProperty("amount")
    private String amount;
    @JsonProperty("reversal")
    private Boolean reversal;
    @JsonProperty("bouquetCode")
    private String bouquetCode;
    @JsonProperty("bouquetName")
    private String bouquetName;
    @JsonProperty("account")
    private String account;
    @JsonProperty("externalReference")
    private String externalReference;
    @JsonProperty("auditReferenceNumber")
    private String auditReferenceNumber;
    @JsonProperty("date")
    private String date;
    @JsonProperty("response")
    private Response response;
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

    @JsonProperty("ref")
    public String getRef() {
        return ref;
    }

    @JsonProperty("ref")
    public void setRef(String ref) {
        this.ref = ref;
    }

    @JsonProperty("amount")
    public String getAmount() {
        return amount;
    }

    @JsonProperty("amount")
    public void setAmount(String amount) {
        this.amount = amount;
    }

    @JsonProperty("reversal")
    public Boolean getReversal() {
        return reversal;
    }

    @JsonProperty("reversal")
    public void setReversal(Boolean reversal) {
        this.reversal = reversal;
    }

    @JsonProperty("bouquetCode")
    public String getBouquetCode() {
        return bouquetCode;
    }

    @JsonProperty("bouquetCode")
    public void setBouquetCode(String bouquetCode) {
        this.bouquetCode = bouquetCode;
    }

    @JsonProperty("bouquetName")
    public String getBouquetName() {
        return bouquetName;
    }

    @JsonProperty("bouquetName")
    public void setBouquetName(String bouquetName) {
        this.bouquetName = bouquetName;
    }

    @JsonProperty("account")
    public String getAccount() {
        return account;
    }

    @JsonProperty("account")
    public void setAccount(String account) {
        this.account = account;
    }

    @JsonProperty("externalReference")
    public String getExternalReference() {
        return externalReference;
    }

    @JsonProperty("externalReference")
    public void setExternalReference(String externalReference) {
        this.externalReference = externalReference;
    }

    @JsonProperty("auditReferenceNumber")
    public String getAuditReferenceNumber() {
        return auditReferenceNumber;
    }

    @JsonProperty("auditReferenceNumber")
    public void setAuditReferenceNumber(String auditReferenceNumber) {
        this.auditReferenceNumber = auditReferenceNumber;
    }

    @JsonProperty("date")
    public String getDate() {
        return date;
    }

    @JsonProperty("date")
    public void setDate(String date) {
        this.date = date;
    }

    @JsonProperty("response")
    public Response getResponse() {
        return response;
    }

    @JsonProperty("response")
    public void setResponse(Response response) {
        this.response = response;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "PurchaseCableTVResponseData{" +
            "status=" + status +
            "error=" + error +
            ", message='" + message + '\'' +
            ", ref='" + ref + '\'' +
            ", amount='" + amount + '\'' +
            ", reversal=" + reversal +
            ", bouquetCode='" + bouquetCode + '\'' +
            ", bouquetName='" + bouquetName + '\'' +
            ", account='" + account + '\'' +
            ", externalReference='" + externalReference + '\'' +
            ", auditReferenceNumber='" + auditReferenceNumber + '\'' +
            ", date='" + date + '\'' +
            ", response=" + response +
            ", responseCode='" + responseCode + '\'' +
            ", reference='" + reference + '\'' +
            ", sequence='" + sequence + '\'' +
            ", clientReference='" + clientReference + '\'' +
            '}';
    }
}
