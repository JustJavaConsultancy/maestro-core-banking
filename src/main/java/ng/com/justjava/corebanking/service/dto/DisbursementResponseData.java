package ng.com.justjava.corebanking.service.dto;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "approval_request",
    "borrower_phone",
    "accountNumber",
    "currency",
    "uniqueRef",
    "loanAmount",
    "collectionAmount",
    "dateOfDisbursement",
    "dateOfCollection",
    "totalCollectionAmount",
    "numberOfRepayments"
})
@Generated("jsonschema2pojo")
public class DisbursementResponseData {

    @JsonProperty("approval_request")
    private Integer approvalRequest;
    @JsonProperty("borrower_phone")
    private String borrowerPhone;
    @JsonProperty("accountNumber")
    private String accountNumber;
    @JsonProperty("currency")
    private String currency;
    @JsonProperty("uniqueRef")
    private String uniqueRef;
    @JsonProperty("loanAmount")
    private Double loanAmount;
    @JsonProperty("collectionAmount")
    private Double collectionAmount;
    @JsonProperty("dateOfDisbursement")
    private String dateOfDisbursement;
    @JsonProperty("dateOfCollection")
    private String dateOfCollection;
    @JsonProperty("totalCollectionAmount")
    private Double totalCollectionAmount;
    @JsonProperty("numberOfRepayments")
    private String numberOfRepayments;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("approval_request")
    public Integer getApprovalRequest() {
        return approvalRequest;
    }

    @JsonProperty("approval_request")
    public void setApprovalRequest(Integer approvalRequest) {
        this.approvalRequest = approvalRequest;
    }

    @JsonProperty("borrower_phone")
    public String getBorrowerPhone() {
        return borrowerPhone;
    }

    @JsonProperty("borrower_phone")
    public void setBorrowerPhone(String borrowerPhone) {
        this.borrowerPhone = borrowerPhone;
    }

    @JsonProperty("accountNumber")
    public String getAccountNumber() {
        return accountNumber;
    }

    @JsonProperty("accountNumber")
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    @JsonProperty("currency")
    public String getCurrency() {
        return currency;
    }

    @JsonProperty("currency")
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @JsonProperty("uniqueRef")
    public String getUniqueRef() {
        return uniqueRef;
    }

    @JsonProperty("uniqueRef")
    public void setUniqueRef(String uniqueRef) {
        this.uniqueRef = uniqueRef;
    }

    @JsonProperty("loanAmount")
    public Double getLoanAmount() {
        return loanAmount;
    }

    @JsonProperty("loanAmount")
    public void setLoanAmount(Double loanAmount) {
        this.loanAmount = loanAmount;
    }

    @JsonProperty("collectionAmount")
    public Double getCollectionAmount() {
        return collectionAmount;
    }

    @JsonProperty("collectionAmount")
    public void setCollectionAmount(Double collectionAmount) {
        this.collectionAmount = collectionAmount;
    }

    @JsonProperty("dateOfDisbursement")
    public String getDateOfDisbursement() {
        return dateOfDisbursement;
    }

    @JsonProperty("dateOfDisbursement")
    public void setDateOfDisbursement(String dateOfDisbursement) {
        this.dateOfDisbursement = dateOfDisbursement;
    }

    @JsonProperty("dateOfCollection")
    public String getDateOfCollection() {
        return dateOfCollection;
    }

    @JsonProperty("dateOfCollection")
    public void setDateOfCollection(String dateOfCollection) {
        this.dateOfCollection = dateOfCollection;
    }

    @JsonProperty("totalCollectionAmount")
    public Double getTotalCollectionAmount() {
        return totalCollectionAmount;
    }

    @JsonProperty("totalCollectionAmount")
    public void setTotalCollectionAmount(Double totalCollectionAmount) {
        this.totalCollectionAmount = totalCollectionAmount;
    }

    @JsonProperty("numberOfRepayments")
    public String getNumberOfRepayments() {
        return numberOfRepayments;
    }

    @JsonProperty("numberOfRepayments")
    public void setNumberOfRepayments(String numberOfRepayments) {
        this.numberOfRepayments = numberOfRepayments;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
