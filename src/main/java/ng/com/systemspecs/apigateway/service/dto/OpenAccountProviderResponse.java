package ng.com.systemspecs.apigateway.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "reference",
    "account_number",
    "contract_code",
    "account_reference",
    "account_name",
    "currency_code",
    "customer_email",
    "bank_name",
    "bank_code",
    "account_type",
    "status",
    "createdOn",
    "meta"
})
@Generated("jsonschema2pojo")
public class OpenAccountProviderResponse {

    @JsonProperty("reference")
    private String reference;
    @JsonProperty("account_number")
    private String accountNumber;
    @JsonProperty("contract_code")
    private String contractCode;
    @JsonProperty("account_reference")
    private String accountReference;
    @JsonProperty("account_name")
    private String accountName;
    @JsonProperty("currency_code")
    private String currencyCode;
    @JsonProperty("customer_email")
    private String customerEmail;
    @JsonProperty("bank_name")
    private String bankName;
    @JsonProperty("bank_code")
    private String bankCode;
    @JsonProperty("account_type")
    private String accountType;
    @JsonProperty("status")
    private String status;
    @JsonProperty("createdOn")
    private String createdOn;
    @JsonProperty("meta")
    private Object meta;

    @JsonProperty("reference")
    public String getReference() {
        return reference;
    }

    @JsonProperty("reference")
    public void setReference(String reference) {
        this.reference = reference;
    }

    @JsonProperty("account_number")
    public String getAccountNumber() {
        return accountNumber;
    }

    @JsonProperty("account_number")
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    @JsonProperty("contract_code")
    public String getContractCode() {
        return contractCode;
    }

    @JsonProperty("contract_code")
    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    @JsonProperty("account_reference")
    public String getAccountReference() {
        return accountReference;
    }

    @JsonProperty("account_reference")
    public void setAccountReference(String accountReference) {
        this.accountReference = accountReference;
    }

    @JsonProperty("account_name")
    public String getAccountName() {
        return accountName;
    }

    @JsonProperty("account_name")
    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    @JsonProperty("currency_code")
    public String getCurrencyCode() {
        return currencyCode;
    }

    @JsonProperty("currency_code")
    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    @JsonProperty("customer_email")
    public String getCustomerEmail() {
        return customerEmail;
    }

    @JsonProperty("customer_email")
    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    @JsonProperty("bank_name")
    public String getBankName() {
        return bankName;
    }

    @JsonProperty("bank_name")
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    @JsonProperty("bank_code")
    public String getBankCode() {
        return bankCode;
    }

    @JsonProperty("bank_code")
    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    @JsonProperty("account_type")
    public String getAccountType() {
        return accountType;
    }

    @JsonProperty("account_type")
    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("created_on")
    public String getCreatedOn() {
        return createdOn;
    }

    @JsonProperty("created_on")
    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    @JsonProperty("meta")
    public Object getMeta() {
        return meta;
    }

    @JsonProperty("meta")
    public void setMeta(Object meta) {
        this.meta = meta;
    }

    @Override
    public String toString() {
        return "OpenAccountProviderResponse{" +
            "reference='" + reference + '\'' +
            ", accountNumber='" + accountNumber + '\'' +
            ", contractCode='" + contractCode + '\'' +
            ", accountReference='" + accountReference + '\'' +
            ", accountName='" + accountName + '\'' +
            ", currencyCode='" + currencyCode + '\'' +
            ", customerEmail='" + customerEmail + '\'' +
            ", bankName='" + bankName + '\'' +
            ", bankCode='" + bankCode + '\'' +
            ", accountType='" + accountType + '\'' +
            ", status='" + status + '\'' +
            ", createdOn='" + createdOn + '\'' +
            ", meta=" + meta +
            '}';
    }
}
