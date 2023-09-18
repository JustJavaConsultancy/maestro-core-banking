package ng.com.justjava.corebanking.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "loan_id",
    "loan_product_id",
    "borrower_id",
    "loan_application_id",
    "loan_disbursed_by_id",
    "loan_principal_amount",
    "loan_released_date",
    "loan_interest_method",
    "loan_interest_type",
    "loan_interest_period",
    "loan_interest",
    "loan_duration_period",
    "loan_duration",
    "loan_payment_scheme_id",
    "loan_num_of_repayments",
    "loan_decimal_places",
    "custom_field_4287"
})
@Generated("jsonschema2pojo")
public class CreateLoanDTO {

    @JsonProperty("loan_id")
    private String loanId;
    //@JsonProperty("loan_product_id")
    //private String loanProductId = "29865";
    @JsonProperty("loan_product_id")
    private String loanProductId = "109236";

    @JsonProperty("loan_status_id")
    private String loanStatusId = "1";
    @JsonProperty("borrower_id")
    private String borrowerId;
    @JsonProperty("loan_application_id")
    private String loanApplicationId;
	/*
	 * @JsonProperty("loan_disbursed_by_id") private String loanDisbursedById =
	 * "79025";
	 */

    @JsonProperty("loan_disbursed_by_id")
    private String loanDisbursedById = "79025";

    @JsonProperty("loan_principal_amount")
    private Double loanPrincipalAmount;
    @JsonProperty("loan_released_date")
    private String loanReleasedDate;
    @JsonProperty("loan_interest_method")
    private String loanInterestMethod = "flat_rate";
    @JsonProperty("loan_interest_type")
    private String loanInterestType = "percentage";
    @JsonProperty("loan_interest_period")
    private String loanInterestPeriod = "Month";
    @JsonProperty("loan_interest")
    private Double loanInterest;
    @JsonProperty("loan_duration_period")
    private String loanDurationPeriod = "Months";
    @JsonProperty("loan_duration")
    private Integer loanDuration;

	/*
	 * @JsonProperty("loan_payment_scheme_id") private String loanPaymentSchemeId =
	 * "1123"; //endOfMonth
	 */
    @JsonProperty("loan_payment_scheme_id")
    private String loanPaymentSchemeId = "3"; //endOfMonth

    @JsonProperty("loan_num_of_repayments")
    private Integer loanNumOfRepayments;
    @JsonProperty("loan_decimal_places")
    private String loanDecimalPlaces = "round_off_to_two_decimal";
    @JsonProperty("custom_field_4287")
    private String customField4287; //required amount

    @JsonProperty("custom_field_6363")
    private String customField6363; //mINISTRY/aGENCY

    @JsonProperty("custom_field_5251")
    private String customField5251; //Remita Mandate Id

    @JsonProperty("loan_id")
    public String getLoanId() {
        return loanId;
    }

    @JsonProperty("loan_status_id")
    public String getLoanStatusId() {
        return loanStatusId;
    }

    @JsonProperty("loan_status_id")
    public void setLoanStatusId(String loanStatusId) {
        this.loanStatusId = loanStatusId;
    }

    @JsonProperty("loan_id")
    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    @JsonProperty("loan_product_id")
    public String getLoanProductId() {
        return loanProductId;
    }

    @JsonProperty("loan_product_id")
    public void setLoanProductId(String loanProductId) {
        this.loanProductId = loanProductId;
    }

    @JsonProperty("borrower_id")
    public String getBorrowerId() {
        return borrowerId;
    }

    @JsonProperty("borrower_id")
    public void setBorrowerId(String borrowerId) {
        this.borrowerId = borrowerId;
    }

    @JsonProperty("loan_application_id")
    public String getLoanApplicationId() {
        return loanApplicationId;
    }

    @JsonProperty("loan_application_id")
    public void setLoanApplicationId(String loanApplicationId) {
        this.loanApplicationId = loanApplicationId;
    }

    @JsonProperty("loan_disbursed_by_id")
    public String getLoanDisbursedById() {
        return loanDisbursedById;
    }

    @JsonProperty("loan_disbursed_by_id")
    public void setLoanDisbursedById(String loanDisbursedById) {
        this.loanDisbursedById = loanDisbursedById;
    }

    @JsonProperty("loan_principal_amount")
    public Double getLoanPrincipalAmount() {
        return loanPrincipalAmount;
    }

    @JsonProperty("loan_principal_amount")
    public void setLoanPrincipalAmount(Double loanPrincipalAmount) {
        this.loanPrincipalAmount = loanPrincipalAmount;
    }

    @JsonProperty("loan_released_date")
    public String getLoanReleasedDate() {
        return loanReleasedDate;
    }

    @JsonProperty("loan_released_date")
    public void setLoanReleasedDate(String loanReleasedDate) {
        this.loanReleasedDate = loanReleasedDate;
    }

    @JsonProperty("loan_interest_method")
    public String getLoanInterestMethod() {
        return loanInterestMethod;
    }

    @JsonProperty("loan_interest_method")
    public void setLoanInterestMethod(String loanInterestMethod) {
        this.loanInterestMethod = loanInterestMethod;
    }

    @JsonProperty("loan_interest_type")
    public String getLoanInterestType() {
        return loanInterestType;
    }

    @JsonProperty("loan_interest_type")
    public void setLoanInterestType(String loanInterestType) {
        this.loanInterestType = loanInterestType;
    }

    @JsonProperty("loan_interest_period")
    public String getLoanInterestPeriod() {
        return loanInterestPeriod;
    }

    @JsonProperty("loan_interest_period")
    public void setLoanInterestPeriod(String loanInterestPeriod) {
        this.loanInterestPeriod = loanInterestPeriod;
    }

    @JsonProperty("loan_interest")
    public Double getLoanInterest() {
        return loanInterest;
    }

    @JsonProperty("loan_interest")
    public void setLoanInterest(Double loanInterest) {
        this.loanInterest = loanInterest;
    }

    @JsonProperty("loan_duration_period")
    public String getLoanDurationPeriod() {
        return loanDurationPeriod;
    }

    @JsonProperty("loan_duration_period")
    public void setLoanDurationPeriod(String loanDurationPeriod) {
        this.loanDurationPeriod = loanDurationPeriod;
    }

    @JsonProperty("loan_duration")
    public Integer getLoanDuration() {
        return loanDuration;
    }

    @JsonProperty("loan_duration")
    public void setLoanDuration(Integer loanDuration) {
        this.loanDuration = loanDuration;
    }

    @JsonProperty("loan_payment_scheme_id")
    public String getLoanPaymentSchemeId() {
        return loanPaymentSchemeId;
    }

    @JsonProperty("loan_payment_scheme_id")
    public void setLoanPaymentSchemeId(String loanPaymentSchemeId) {
        this.loanPaymentSchemeId = loanPaymentSchemeId;
    }

    @JsonProperty("loan_num_of_repayments")
    public Integer getLoanNumOfRepayments() {
        return loanNumOfRepayments;
    }

    @JsonProperty("loan_num_of_repayments")
    public void setLoanNumOfRepayments(Integer loanNumOfRepayments) {
        this.loanNumOfRepayments = loanNumOfRepayments;
    }

    @JsonProperty("loan_decimal_places")
    public String getLoanDecimalPlaces() {
        return loanDecimalPlaces;
    }

    @JsonProperty("loan_decimal_places")
    public void setLoanDecimalPlaces(String loanDecimalPlaces) {
        this.loanDecimalPlaces = loanDecimalPlaces;
    }

    @JsonProperty("custom_field_4287")
    public String getCustomField4287() {
        return customField4287;
    }

    @JsonProperty("custom_field_4287")
    public void setCustomField4287(String customField4287) {
        this.customField4287 = customField4287;
    }

    @JsonProperty("custom_field_6363")
    public String getCustomField6363() {
        return customField6363;
    }

    @JsonProperty("custom_field_6363")
    public void setCustomField6363(String customField6363) {
        this.customField6363 = customField6363;
    }

    @JsonProperty("custom_field_5251")
    public String getCustomField5251() {
        return customField5251;
    }

    @JsonProperty("custom_field_5251")
    public void setCustomField5251(String customField5251) {
        this.customField5251 = customField5251;
    }

    @Override
    public String toString() {
        return "CreateLoanDTO{" +
            "loanId='" + loanId + '\'' +
            ", loanProductId='" + loanProductId + '\'' +
            ", borrowerId='" + borrowerId + '\'' +
            ", loanApplicationId='" + loanApplicationId + '\'' +
            ", loanDisbursedById='" + loanDisbursedById + '\'' +
            ", loanPrincipalAmount=" + loanPrincipalAmount +
            ", loanReleasedDate='" + loanReleasedDate + '\'' +
            ", loanInterestMethod='" + loanInterestMethod + '\'' +
            ", loanInterestType='" + loanInterestType + '\'' +
            ", loanInterestPeriod='" + loanInterestPeriod + '\'' +
            ", loanInterest=" + loanInterest +
            ", loanDurationPeriod='" + loanDurationPeriod + '\'' +
            ", loanDuration=" + loanDuration +
            ", loanPaymentSchemeId='" + loanPaymentSchemeId + '\'' +
            ", loanNumOfRepayments=" + loanNumOfRepayments +
            ", loanDecimalPlaces='" + loanDecimalPlaces + '\'' +
            ", requiredAmount='" + customField4287 + '\'' +
            ", Ministry='" + customField6363 + '\'' +
            ", Remita Mandate='" + customField5251 + '\'' +
            '}';
    }
}
