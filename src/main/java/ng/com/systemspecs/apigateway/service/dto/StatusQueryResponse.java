package ng.com.systemspecs.apigateway.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "responseCode",
    "reference",
    "amount",
    "terminalId",
    "merchantId",
    "responsemessage",
    "retrievalReference",
    "institutionCode",
    "shortName",
    "customer_mobile",
    "subMerchantName",
    "Tnx",
    "TransactionID",
    "TraceID",
    "UserID"
})
public class StatusQueryResponse {
    @JsonProperty("responseCode")
    private String responseCode;
    @JsonProperty("reference")
    private String reference;
    @JsonProperty("amount")
    private Integer amount;
    @JsonProperty("terminalId")
    private String terminalId;
    @JsonProperty("merchantId")
    private String merchantId;
    @JsonProperty("responsemessage")
    private String responsemessage;
    @JsonProperty("retrievalReference")
    private String retrievalReference;
    @JsonProperty("institutionCode")
    private String institutionCode;
    @JsonProperty("shortName")
    private String shortName;
    @JsonProperty("customer_mobile")
    private String customerMobile;
    @JsonProperty("subMerchantName")
    private String subMerchantName;
    @JsonProperty("Tnx")
    private String tnx;
    @JsonProperty("TransactionID")
    private String transactionID;
    @JsonProperty("TraceID")
    private String traceID;
    @JsonProperty("UserID")
    private String userID;

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

    @JsonProperty("amount")
    public Integer getAmount() {
        return amount;
    }

    @JsonProperty("amount")
    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    @JsonProperty("terminalId")
    public String getTerminalId() {
        return terminalId;
    }

    @JsonProperty("terminalId")
    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    @JsonProperty("merchantId")
    public String getMerchantId() {
        return merchantId;
    }

    @JsonProperty("merchantId")
    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    @JsonProperty("responsemessage")
    public String getResponsemessage() {
        return responsemessage;
    }

    @JsonProperty("responsemessage")
    public void setResponsemessage(String responsemessage) {
        this.responsemessage = responsemessage;
    }

    @JsonProperty("retrievalReference")
    public String getRetrievalReference() {
        return retrievalReference;
    }

    @JsonProperty("retrievalReference")
    public void setRetrievalReference(String retrievalReference) {
        this.retrievalReference = retrievalReference;
    }

    @JsonProperty("institutionCode")
    public String getInstitutionCode() {
        return institutionCode;
    }

    @JsonProperty("institutionCode")
    public void setInstitutionCode(String institutionCode) {
        this.institutionCode = institutionCode;
    }

    @JsonProperty("shortName")
    public String getShortName() {
        return shortName;
    }

    @JsonProperty("shortName")
    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    @JsonProperty("customer_mobile")
    public String getCustomerMobile() {
        return customerMobile;
    }

    @JsonProperty("customer_mobile")
    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    @JsonProperty("subMerchantName")
    public String getSubMerchantName() {
        return subMerchantName;
    }

    @JsonProperty("subMerchantName")
    public void setSubMerchantName(String subMerchantName) {
        this.subMerchantName = subMerchantName;
    }

    @JsonProperty("Tnx")
    public String getTnx() {
        return tnx;
    }

    @JsonProperty("Tnx")
    public void setTnx(String tnx) {
        this.tnx = tnx;
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

    @JsonProperty("UserID")
    public String getUserID() {
        return userID;
    }

    @JsonProperty("UserID")
    public void setUserID(String userID) {
        this.userID = userID;
    }

}
