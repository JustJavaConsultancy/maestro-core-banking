package ng.com.justjava.corebanking.service.dto;

public class CoralPayTransactionStatusDTO {

    private String responseCode;
    private String responsemessage;
    private String reference;
    private Double amount;
    private String terminalId;
    private String merchantId;
    private String retrievalReference;
    private String institutionCode;
    private String shortName;
    private String customerMobile;
    private String subMerchantName;
    private String transactionID;
    private String userID;
    private String traceID;

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponsemessage() {
        return responsemessage;
    }

    public void setResponsemessage(String responsemessage) {
        this.responsemessage = responsemessage;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getRetrievalReference() {
        return retrievalReference;
    }

    public void setRetrievalReference(String retrievalReference) {
        this.retrievalReference = retrievalReference;
    }

    public String getInstitutionCode() {
        return institutionCode;
    }

    public void setInstitutionCode(String institutionCode) {
        this.institutionCode = institutionCode;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    public String getSubMerchantName() {
        return subMerchantName;
    }

    public void setSubMerchantName(String subMerchantName) {
        this.subMerchantName = subMerchantName;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getTraceID() {
        return traceID;
    }

    public void setTraceID(String traceID) {
        this.traceID = traceID;
    }

    @Override
    public String toString() {
        return "CoralPayTransactionStatusDTO{" +
            "responseCode='" + responseCode + '\'' +
            ", responsemessage='" + responsemessage + '\'' +
            ", reference='" + reference + '\'' +
            ", amount=" + amount +
            ", terminalId='" + terminalId + '\'' +
            ", merchantId='" + merchantId + '\'' +
            ", retrievalReference='" + retrievalReference + '\'' +
            ", institutionCode='" + institutionCode + '\'' +
            ", shortName='" + shortName + '\'' +
            ", customerMobile='" + customerMobile + '\'' +
            ", subMerchantName='" + subMerchantName + '\'' +
            ", transactionID='" + transactionID + '\'' +
            ", userID='" + userID + '\'' +
            ", traceID='" + traceID + '\'' +
            '}';
    }
}
