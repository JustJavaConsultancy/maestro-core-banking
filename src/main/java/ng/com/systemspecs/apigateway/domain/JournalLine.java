package ng.com.systemspecs.apigateway.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ng.com.systemspecs.apigateway.domain.enumeration.PaymentType;
import ng.com.systemspecs.apigateway.domain.enumeration.TransactionStatus;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "jounal_line")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonIgnoreProperties(value = {"walletAccount","jounal"})
public class JournalLine {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    private Double currentBalance;

    @ManyToOne
    private WalletAccount walletAccount;
    @Column(columnDefinition = "double default 0.00")
    private Double debit;

    @Column(columnDefinition = "double default 0.00")
    private Double credit;

    @ManyToOne
    @JsonIgnoreProperties(value = "journalLines", allowSetters = true)
    private Journal jounal;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getDebit() {
        return Double.valueOf(formatMoney(debit));
    }

    public void setDebit(Double debit) {
        this.debit = debit;
    }

    public Double getCredit() {
        return Double.valueOf(formatMoney(credit));
    }

    public void setCredit(Double credit) {
        this.credit = credit;
    }

    public Journal getJounal() {
        return jounal;
    }

    public void setJounal(Journal jounal) {
        this.jounal = jounal;
    }

    public WalletAccount getWalletAccount() {
        return walletAccount;
    }

    public void setWalletAccount(WalletAccount walletAccount) {
        this.walletAccount = walletAccount;
    }

    public String getCreditDebit() {
        return (debit == 0 && credit > 0.00) ? "credit" : "debit";
    }

    public Double getAmount() {
        return (debit == 0 && credit > 0.00) ? Double.valueOf(formatMoney(credit)) : Double.valueOf(formatMoney(debit));

    }

    public String getTransactionRef() {
        return jounal.getReference().toUpperCase();
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    public LocalDateTime getTransactionDate() {
        return jounal.getTransDate();
    }

    public String getMemo() {
        return jounal.getDisplayMemo();
    }

    public String getAccountName() {
        if (walletAccount != null) {
            return walletAccount.getAccountName();
        }
        return "";
    }

    public String getAccountNumber() {
        if (walletAccount != null) {
            return walletAccount.getAccountNumber();
        }
        return "";
    }

    public Double getCurrentBalance() {
        String s = formatMoney(currentBalance);
        if (s != null) {
            return Double.valueOf(s);
        }
        return currentBalance;
    }

    public void setCurrentBalance(Double currentBalance) {
        if (currentBalance == null) {
            currentBalance = 0.00;
        }
        this.currentBalance = currentBalance;
    }

    public TransactionStatus getTransactionStatus() {
        return jounal.getTransactionStatus();
    }

    public PaymentType getPaymentType() {
        return jounal.getPaymentType();
    }

    public String getExternalRef() {
        return jounal.getExternalRef();
    }

    public String getUserComment() {
        return jounal.getUserComment();
    }


    @Override
    public String toString() {
        return "JournalLine{" +
            "id=" + id +
            ", currentBalance=" + getCurrentBalance() +
            ", debit=" + getDebit() +
            ", credit=" + getCredit() +
            '}';
    }

    public static String formatMoney(Double amount) {
        if (amount == null) {
            return null;
        }
        return String.format("%.2f", amount);
    }
}
