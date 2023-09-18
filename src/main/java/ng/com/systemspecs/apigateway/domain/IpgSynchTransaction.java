package ng.com.systemspecs.apigateway.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ipg_synch_transaction")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class IpgSynchTransaction {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "channel")
    private String channel;

    @Column(name = "currency")
    private String currency;

    @Column(name = "source_account")
    private String sourceAccount;

    @Column(name = "source_account_bank_code")
    private String sourceAccountBankCode;

    @Column(name = "source_account_name")
    private String sourceAccountName;

    @Column(name = "source_narration")
    private String sourceNarration;

    @Column(name = "destination_account")
    private String destinationAccount;

    @Column(name = "destination_account_bank_code")
    private String destinationAccountBankCode;

    @Column(name = "destination_account_name")
    private String destinationAccountName;

    @Column(name = "destination_narration")
    private String destinationNarration;

    @Column(name = "transaction_ref")
    private String transactionRef;

    @Column(name = "transaction_type")
    private String transactionType;

    @Column(name = "fee_amount")
    private BigDecimal feeAmount;

    @Column(name = "vat_amount")
    private BigDecimal vatAmount;

    @Column(name = "transaction_auth_id")
    private String transactionAuthId;

    @Column(name = "transaction_signature")
    private String transactionSignature;

    @Column(name = "partner_id")
    private String partnerId;

    @Column(name = "total_amount_debited")
    private BigDecimal totalAmountDebited;

    @Column(name = "reversal_ref")
    private String reversalRef;

    @Column(name = "reversed")
    private Boolean reversed;

    @Column(name = "trans_date")
    private LocalDateTime transDate = LocalDateTime.now();

    @Column(name = "status")
    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getSourceAccount() {
        return sourceAccount;
    }

    public void setSourceAccount(String sourceAccount) {
        this.sourceAccount = sourceAccount;
    }

    public String getSourceAccountBankCode() {
        return sourceAccountBankCode;
    }

    public void setSourceAccountBankCode(String sourceAccountBankCode) {
        this.sourceAccountBankCode = sourceAccountBankCode;
    }

    public String getSourceAccountName() {
        return sourceAccountName;
    }

    public void setSourceAccountName(String sourceAccountName) {
        this.sourceAccountName = sourceAccountName;
    }

    public String getSourceNarration() {
        return sourceNarration;
    }

    public void setSourceNarration(String sourceNarration) {
        this.sourceNarration = sourceNarration;
    }

    public String getDestinationAccount() {
        return destinationAccount;
    }

    public void setDestinationAccount(String destinationAccount) {
        this.destinationAccount = destinationAccount;
    }

    public String getDestinationAccountBankCode() {
        return destinationAccountBankCode;
    }

    public void setDestinationAccountBankCode(String destinationAccountBankCode) {
        this.destinationAccountBankCode = destinationAccountBankCode;
    }

    public String getDestinationAccountName() {
        return destinationAccountName;
    }

    public void setDestinationAccountName(String destinationAccountName) {
        this.destinationAccountName = destinationAccountName;
    }

    public String getDestinationNarration() {
        return destinationNarration;
    }

    public void setDestinationNarration(String destinationNarration) {
        this.destinationNarration = destinationNarration;
    }

    public String getTransactionRef() {
        return transactionRef;
    }

    public void setTransactionRef(String transactionRef) {
        this.transactionRef = transactionRef;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public BigDecimal getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(BigDecimal feeAmount) {
        this.feeAmount = feeAmount;
    }

    public BigDecimal getVatAmount() {
        return vatAmount;
    }

    public void setVatAmount(BigDecimal vatAmount) {
        this.vatAmount = vatAmount;
    }

    public String getTransactionAuthId() {
        return transactionAuthId;
    }

    public void setTransactionAuthId(String transactionAuthId) {
        this.transactionAuthId = transactionAuthId;
    }

    public String getTransactionSignature() {
        return transactionSignature;
    }

    public void setTransactionSignature(String transactionSignature) {
        this.transactionSignature = transactionSignature;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public BigDecimal getTotalAmountDebited() {
        return totalAmountDebited;
    }

    public void setTotalAmountDebited(BigDecimal totalAmountDebited) {
        this.totalAmountDebited = totalAmountDebited;
    }

    public String getReversalRef() {
        return reversalRef;
    }

    public void setReversalRef(String reversalRef) {
        this.reversalRef = reversalRef;
    }

    public Boolean getReversed() {
        return reversed;
    }

    public void setReversed(Boolean reversed) {
        this.reversed = reversed;
    }

    public LocalDateTime getTransDate() {
        return transDate;
    }

    public void setTransDate(LocalDateTime transDate) {
        this.transDate = transDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "IpgSynchTransaction{" +
            "id=" + id +
            ", amount=" + amount +
            ", channel='" + channel + '\'' +
            ", currency='" + currency + '\'' +
            ", sourceAccount='" + sourceAccount + '\'' +
            ", sourceAccountBankCode='" + sourceAccountBankCode + '\'' +
            ", sourceAccountName='" + sourceAccountName + '\'' +
            ", sourceNarration='" + sourceNarration + '\'' +
            ", destinationAccount='" + destinationAccount + '\'' +
            ", destinationAccountBankCode='" + destinationAccountBankCode + '\'' +
            ", destinationAccountName='" + destinationAccountName + '\'' +
            ", destinationNarration='" + destinationNarration + '\'' +
            ", transactionRef='" + transactionRef + '\'' +
            ", transactionType='" + transactionType + '\'' +
            ", feeAmount=" + feeAmount +
            ", vatAmount=" + vatAmount +
            ", transactionAuthId='" + transactionAuthId + '\'' +
            ", transactionSignature='" + transactionSignature + '\'' +
            ", partnerId='" + partnerId + '\'' +
            ", totalAmountDebited=" + totalAmountDebited +
            ", reversalRef='" + reversalRef + '\'' +
            ", reversed=" + reversed +
            ", transDate=" + transDate +
            ", status=" + status +
            '}';
    }
}
