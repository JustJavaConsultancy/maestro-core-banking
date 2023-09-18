package ng.com.justjava.corebanking.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ng.com.justjava.corebanking.domain.enumeration.LoanStatus;
import ng.com.justjava.corebanking.domain.enumeration.LoanType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * A Loan.
 */
@Entity
@Table(name = "loan")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Loan extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "tenure")
    private String tenure;

    @Column(name = "loan_id")
    private String loanId;

    @Column(name = "eligibility_amount")
    private Double eligibilityAmount;

    @Column(name = "customer_id")
    private String customerId;

    @Column(name = "customer_account_no")
    private String customerAccountNo;

    @Column(name = "customer_bank_code")
    private String customerBankCode;

    @Column(name = "borrower_id")
    private String borrowerId;

    @Column(name = "mandate_reference")
    private String mandateReference;

    @Column(name = "ministry")
    private String ministry;

    @Column(name = "approval_id")
    private String approvalId;

    @Column(name = "account_number")
    private String accountNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private LoanStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "loan_type")
    private LoanType loanType;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "loans", allowSetters = true)
    private Profile profile;

    @ManyToOne
    @JsonIgnoreProperties(value = "loans", allowSetters = true)
    private Lender lender;

    @Column(name = "interest_rate")
    private String interestRate;

    @Column(name = "duration")
    private String duration;

    @Column(name = "number_of_repayments")
    private String numberOfRepayments;

    @Column(name = "collection_amount")
    private String collectionAmount;

    @Column(name = "total_collection_amount")
    private String totalCollectionAmount;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public Loan amount(Double amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getTenure() {
        return tenure;
    }

    public Loan tenure(String tenure) {
        this.tenure = tenure;
        return this;
    }

    public void setTenure(String tenure) {
        this.tenure = tenure;
    }

    public LoanStatus getStatus() {
        return status;
    }

    public Loan status(LoanStatus status) {
        this.status = status;
        return this;
    }

    public String getBorrowerId() {
        return borrowerId;
    }

    public void setBorrowerId(String borrowerId) {
        this.borrowerId = borrowerId;
    }

    public void setStatus(LoanStatus status) {
        this.status = status;
    }

    public Profile getProfile() {
        return profile;
    }

    public Loan profile(Profile profile) {
        this.profile = profile;
        return this;
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

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Lender getLender() {
        return lender;
    }

    public Loan lender(Lender lender) {
        this.lender = lender;
        return this;
    }

    public void setLender(Lender lender) {
        this.lender = lender;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here


    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    public LoanType getLoanType() {
        return loanType;
    }

    public void setLoanType(LoanType loanType) {
        this.loanType = loanType;
    }

    public String getMandateReference() {
        return mandateReference;
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

    public String getApprovalId() {
        return approvalId;
    }

    public void setApprovalId(String approvalId) {
        this.approvalId = approvalId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Loan)) {
            return false;
        }
        return id != null && id.equals(((Loan) o).id);
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

    public String getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(String interestRate) {
        this.interestRate = interestRate;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getNumberOfRepayments() {
        return numberOfRepayments;
    }

    public void setNumberOfRepayments(String numberOfRepayments) {
        this.numberOfRepayments = numberOfRepayments;
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

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore

    @Override
    public String toString() {
        return "Loan{" +
            "id=" + id +
            ", amount=" + amount +
            ", tenure='" + tenure + '\'' +
            ", loanId='" + loanId + '\'' +
            ", eligibilityAmount=" + eligibilityAmount +
            ", customerId='" + customerId + '\'' +
            ", customerAccountNo='" + customerAccountNo + '\'' +
            ", customerBankCode='" + customerBankCode + '\'' +
            ", mandateReference='" + mandateReference + '\'' +
            ", ministry='" + ministry + '\'' +
            ", approvalId='" + approvalId + '\'' +
            ", accountNumber='" + accountNumber + '\'' +
            ", status=" + status +
            ", loanType=" + loanType +
            ", profile=" + profile +
            ", lender=" + lender +
            ", interestRate='" + interestRate + '\'' +
            ", duration='" + duration + '\'' +
            ", numberOfRepayments='" + numberOfRepayments + '\'' +
            ", collectionAmount='" + collectionAmount + '\'' +
            ", totalCollectionAmount='" + totalCollectionAmount + '\'' +
            '}';
    }
}
