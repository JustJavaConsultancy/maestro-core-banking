
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
    "loan_verified",
    "repayment_verified",
    "borrower_verified",
    "approval_verified",
    "verified_ok",
    "mandateReference"
})
@Generated("jsonschema2pojo")
public class DisbursementResponse {

    @JsonProperty("loan_verified")
    private Boolean loanVerified;
    @JsonProperty("repayment_verified")
    private Boolean repaymentVerified;
    @JsonProperty("borrower_verified")
    private Boolean borrowerVerified;
    @JsonProperty("approval_verified")
    private Boolean approvalVerified;
    @JsonProperty("verified_ok")
    private Boolean verifiedOk;
    @JsonProperty("mandateReference")
    private String mandateReference;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("loan_verified")
    public Boolean getLoanVerified() {
        return loanVerified;
    }

    @JsonProperty("loan_verified")
    public void setLoanVerified(Boolean loanVerified) {
        this.loanVerified = loanVerified;
    }

    @JsonProperty("repayment_verified")
    public Boolean getRepaymentVerified() {
        return repaymentVerified;
    }

    @JsonProperty("repayment_verified")
    public void setRepaymentVerified(Boolean repaymentVerified) {
        this.repaymentVerified = repaymentVerified;
    }

    @JsonProperty("borrower_verified")
    public Boolean getBorrowerVerified() {
        return borrowerVerified;
    }

    @JsonProperty("borrower_verified")
    public void setBorrowerVerified(Boolean borrowerVerified) {
        this.borrowerVerified = borrowerVerified;
    }

    @JsonProperty("approval_verified")
    public Boolean getApprovalVerified() {
        return approvalVerified;
    }

    @JsonProperty("approval_verified")
    public void setApprovalVerified(Boolean approvalVerified) {
        this.approvalVerified = approvalVerified;
    }

    @JsonProperty("verified_ok")
    public Boolean getVerifiedOk() {
        return verifiedOk;
    }

    @JsonProperty("verified_ok")
    public void setVerifiedOk(Boolean verifiedOk) {
        this.verifiedOk = verifiedOk;
    }

    @JsonProperty("mandateReference")
    public String getMandateReference() {
        return mandateReference;
    }

    @JsonProperty("mandateReference")
    public void setMandateReference(String mandateReference) {
        this.mandateReference = mandateReference;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public String toString() {
        return "DisbursementResponse{" +
            "loanVerified=" + loanVerified +
            ", repaymentVerified=" + repaymentVerified +
            ", borrowerVerified=" + borrowerVerified +
            ", approvalVerified=" + approvalVerified +
            ", verifiedOk=" + verifiedOk +
            ", mandateReference=" + mandateReference +
            ", additionalProperties=" + additionalProperties +
            '}';
    }
}
