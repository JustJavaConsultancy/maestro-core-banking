package ng.com.systemspecs.apigateway.service.dto;

import ng.com.systemspecs.apigateway.domain.JournalLine;
import ng.com.systemspecs.apigateway.domain.enumeration.PaymentType;
import ng.com.systemspecs.apigateway.domain.enumeration.TransactionStatus;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class JournalDTO implements Serializable {

    private Long id;

    private Double changes;

    private LocalDateTime dueDate;

    private PaymentType paymentType;

    private String reference;

    private String memo;

    private String narration;

    private String externalRef;

    private LocalDateTime transDate;

    private TransactionStatus transactionStatus;

    private String userComment;

    private Set<JournalLine> journalLines = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Double getChanges() {
        return changes;
    }

    public void setChanges(Double changes) {
        this.changes = changes;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public String getExternalRef() {
        return externalRef;
    }

    public void setExternalRef(String externalRef) {
        this.externalRef = externalRef;
    }

    public LocalDateTime getTransDate() {
        return transDate;
    }

    public void setTransDate(LocalDateTime transDate) {
        this.transDate = transDate;
    }

    public TransactionStatus getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(TransactionStatus transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public String getUserComment() {
        return userComment;
    }

    public void setUserComment(String userComment) {
        this.userComment = userComment;
    }

    public Set<JournalLine> getJournalLines() {
        return journalLines;
    }

    public void setJournalLines(Set<JournalLine> journalLines) {
        this.journalLines = journalLines;
    }

    @Override
    public String toString() {
        return "JournalDTO{" +
            "id=" + id +
            ", changes=" + changes +
            ", dueDate=" + dueDate +
            ", paymentType=" + paymentType +
            ", reference='" + reference + '\'' +
            ", memo='" + memo + '\'' +
            ", narration='" + narration + '\'' +
            ", externalRef='" + externalRef + '\'' +
            ", transDate=" + transDate +
            ", transactionStatus=" + transactionStatus +
            ", userComment='" + userComment + '\'' +
            ", journalLines=" + journalLines +
            '}';
    }
}
