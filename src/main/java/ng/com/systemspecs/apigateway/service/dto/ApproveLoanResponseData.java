package ng.com.systemspecs.apigateway.service.dto;

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
    "borrower",
    "loan_amount",
    "approval_status",
    "repayment_amount",
    "approval_id"
})
@Generated("jsonschema2pojo")
public class ApproveLoanResponseData {

    @JsonProperty("borrower")
    private String borrower;
    @JsonProperty("loan_amount")
    private Double loanAmount;
    @JsonProperty("approval_status")
    private Boolean approvalStatus;
    @JsonProperty("repayment_amount")
    private Integer repaymentAmount;
    @JsonProperty("approval_id")
    private Integer approvalId;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("borrower")
    public String getBorrower() {
        return borrower;
    }

    @JsonProperty("borrower")
    public void setBorrower(String borrower) {
        this.borrower = borrower;
    }

    @JsonProperty("loan_amount")
    public Double getLoanAmount() {
        return loanAmount;
    }

    @JsonProperty("loan_amount")
    public void setLoanAmount(Double loanAmount) {
        this.loanAmount = loanAmount;
    }

    @JsonProperty("approval_status")
    public Boolean getApprovalStatus() {
        return approvalStatus;
    }

    @JsonProperty("approval_status")
    public void setApprovalStatus(Boolean approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    @JsonProperty("repayment_amount")
    public Integer getRepaymentAmount() {
        return repaymentAmount;
    }

    @JsonProperty("repayment_amount")
    public void setRepaymentAmount(Integer repaymentAmount) {
        this.repaymentAmount = repaymentAmount;
    }

    @JsonProperty("approval_id")
    public Integer getApprovalId() {
        return approvalId;
    }

    @JsonProperty("approval_id")
    public void setApprovalId(Integer approvalId) {
        this.approvalId = approvalId;
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
