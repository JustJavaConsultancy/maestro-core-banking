package ng.com.justjava.corebanking.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;
import javax.validation.constraints.NotEmpty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "borrower_id"
})
@Generated("jsonschema2pojo")
public class LoanDetailsDTO {

    @JsonProperty("loan_id")
    @NotEmpty(message = "LenderId cannot be null")
    private String loanId;

    @JsonProperty("loan_id")
    public String getLoanId() {
        return loanId;
    }

    @JsonProperty("loan_id")
    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    @Override
    public String toString() {
        return "LoanDetailsDTO{" +
            "loanId='" + loanId + '\'' +
            '}';
    }
}
