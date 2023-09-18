package ng.com.justjava.corebanking.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import ng.com.justjava.corebanking.domain.Loan;
import ng.com.justjava.corebanking.domain.enumeration.LoanStatus;
import ng.com.justjava.corebanking.domain.enumeration.LoanType;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * A DTO for the {@link Loan} entity.
 */
public class LoanDTO implements Serializable {

    private Long id;

    private Double amount;

    private String duration;

    private String tenure;

    private LoanStatus status;

    private LoanType loanType;

    private Long profileId;

    private Long lenderId;

    private String loanId;

    private Double eligibilityAmount;

    private String customerId;

    private String phoneNumber;

    private String customerAccountNo;

    private String customerBankCode;

    private String borrowerId;

    private String mandateReference;

    private String ministry;

    private String accountNumber;

    private String interestRate;

    private String number0fRepayments;

    private String collectionAmount;

    private String totalCollectionAmount;

    private String approvalId;

    @JsonIgnore
    private Instant createdDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    public LocalDateTime getDateCreated() {
        if (createdDate != null) {
            return LocalDateTime.ofInstant(createdDate, ZoneId.systemDefault());
        }

        return null;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getMandateReference() {
        return mandateReference;
    }

    public LoanType getLoanType() {
        return loanType;
    }

    public void setLoanType(LoanType loanType) {
        this.loanType = loanType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getTenure() {
        return tenure;
    }

    public void setTenure(String tenure) {
        this.tenure = tenure;
    }

    public LoanStatus getStatus() {
        return status;
    }

    public void setStatus(LoanStatus status) {
        this.status = status;
    }

    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }

    public Long getLenderId() {
        return lenderId;
    }

    public void setLenderId(Long lenderId) {
        this.lenderId = lenderId;
    }

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    public Double getEligibilityAmount() {
        return eligibilityAmount;
    }

    public void setEligibilityAmount(Double eligibilityAmount) {
        this.eligibilityAmount = eligibilityAmount;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    public String getCustomerAccountNo() {
        return customerAccountNo;
    }

    public void setCustomerAccountNo(String customerAccountNo) {
        this.customerAccountNo = customerAccountNo;
    }

    public String getCustomerBankCode() {
        return customerBankCode;
    }

    public void setCustomerBankCode(String customerBankCode) {
        this.customerBankCode = customerBankCode;
    }

    public String getBorrowerId() {
        return borrowerId;
    }

    public void setBorrowerId(String borrowerId) {
        this.borrowerId = borrowerId;
    }

    public void setMandateReference(String mandateReference) {
        this.mandateReference = mandateReference;
    }

    public String getMinistry() {
        return ministry;
    }

    public void setMinistry(String ministry) {
        this.ministry = ministry;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(String interestRate) {
        this.interestRate = interestRate;
    }

    public String getNumberOfRepayments() {
        return number0fRepayments;
    }

    public void setNumberOfRepayments(String numberOfRepayments) {
        this.number0fRepayments = numberOfRepayments;
    }

    public String getCollectionAmount() {
        return collectionAmount;
    }

    public void setCollectionAmount(String collectionAmount) {
        this.collectionAmount = collectionAmount;
    }

    public String getTotalCollectionAmount() {
        return totalCollectionAmount;
    }

    public void setTotalCollectionAmount(String totalCollectionAmount) {
        this.totalCollectionAmount = totalCollectionAmount;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public String getApprovalId() {
        return approvalId;
    }

    public void setApprovalId(String approvalId) {
        this.approvalId = approvalId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LoanDTO)) {
            return false;
        }

        return id != null && id.equals(((LoanDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore

    @Override
    public String toString() {
        return "LoanDTO{" +
            "id=" + id +
            ", amount=" + amount +
            ", duration='" + duration + '\'' +
            ", tenure='" + tenure + '\'' +
            ", status=" + status +
            ", loanType=" + loanType +
            ", profileId=" + profileId +
            ", lenderId=" + lenderId +
            ", loanId='" + loanId + '\'' +
            ", eligibilityAmount=" + eligibilityAmount +
            ", customerId='" + customerId + '\'' +
            ", phoneNumber='" + phoneNumber + '\'' +
            ", customerAccountNo='" + customerAccountNo + '\'' +
            ", customerBankCode='" + customerBankCode + '\'' +
            ", borrowerId='" + borrowerId + '\'' +
            ", mandateReference='" + mandateReference + '\'' +
            ", ministry='" + ministry + '\'' +
            ", accountNumber='" + accountNumber + '\'' +
            ", interestRate='" + interestRate + '\'' +
            ", number0fRepayments='" + number0fRepayments + '\'' +
            ", collectionAmount='" + collectionAmount + '\'' +
            ", totalCollectionAmount='" + totalCollectionAmount + '\'' +
            ", approvalId='" + approvalId + '\'' +
            ", createdDate=" + createdDate +
            '}';
    }
}
