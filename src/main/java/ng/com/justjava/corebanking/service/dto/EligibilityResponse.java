package ng.com.justjava.corebanking.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "eligible_amount",
    "message",
    "customer_id",
    "customer_account_no",
    "customer_bank_code",
    "intrest",
    "duration"
})
@Generated("jsonschema2pojo")
public class EligibilityResponse {

    @JsonProperty("eligible_amount")
    private Double eligibleAmount;
    @JsonProperty("message")
    private String message;
    @JsonProperty("customer_id")
    private String customerId;
    @JsonProperty("customer_account_no")
    private String customerAccountNo;
    @JsonProperty("customer_bank_code")
    private String customerBankCode;
    @JsonProperty("ministry")
    private String ministry;
    @JsonProperty("company")
    private String company;
    @JsonProperty("interest")
    private String interest;
    @JsonProperty("duration")
    private String duration;

    @JsonProperty("eligible_amount")
    public Double getEligibleAmount() {
        return eligibleAmount;
    }

    @JsonProperty("eligible_amount")
    public void setEligibleAmount(Double eligibleAmount) {
        this.eligibleAmount = eligibleAmount;
    }

    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    @JsonProperty("message")
    public void setMessage(String message) {
        this.message = message;
    }

    @JsonProperty("customer_id")
    public String getCustomerId() {
        return customerId;
    }

    @JsonProperty("customer_id")
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    @JsonProperty("customer_account_no")
    public String getCustomerAccountNo() {
        return customerAccountNo;
    }

    @JsonProperty("customer_account_no")
    public void setCustomerAccountNo(String customerAccountNo) {
        this.customerAccountNo = customerAccountNo;
    }

    @JsonProperty("customer_bank_code")
    public String getCustomerBankCode() {
        return customerBankCode;
    }

    @JsonProperty("customer_bank_code")
    public void setCustomerBankCode(String customerBankCode) {
        this.customerBankCode = customerBankCode;
    }

    public String getMinistry() {
        return ministry;
    }

    public void setMinistry(String ministry) {
        this.ministry = ministry;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    @JsonProperty("interest")
    public String getInterest() {
        return interest;
    }

    @JsonProperty("interest")
    public void setInterest(String interest) {
        this.interest = interest;
    }

    @JsonProperty("duration")
    public String getDuration() {
        return duration;
    }

    @JsonProperty("duration")
    public void setDuration(String duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "EligibilityResponse{" +
            "eligibleAmount=" + eligibleAmount +
            ", message='" + message + '\'' +
            ", customerId='" + customerId + '\'' +
            ", customerAccountNo='" + customerAccountNo + '\'' +
            ", customerBankCode='" + customerBankCode + '\'' +
            ", ministry='" + ministry + '\'' +
            ", company='" + company + '\'' +
            ", interest='" + interest + '\'' +
            ", duration='" + duration + '\'' +
            '}';
    }
}
