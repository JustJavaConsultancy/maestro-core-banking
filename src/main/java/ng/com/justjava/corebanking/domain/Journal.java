package ng.com.justjava.corebanking.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import ng.com.justjava.corebanking.domain.enumeration.PaymentType;
import ng.com.justjava.corebanking.domain.enumeration.TransactionStatus;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "jounal")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Journal {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    private Double changes;

    @Column(columnDefinition = "TIMESTAMP", name = "due_date")
    private LocalDateTime dueDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type")
    private PaymentType paymentType;

    private String reference;
    private String memo;

    @Column(name = "narration")
    private String narration;

    @Column(name = "external_ref")
    private String externalRef;

    @Column(columnDefinition = "TIMESTAMP", name = "trans_date")
    private LocalDateTime transDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "trans_status", columnDefinition = "varchar(20) default 'OK'")
    private TransactionStatus transactionStatus = TransactionStatus.OK;

    @Column(name = "comment")
    private String userComment;

    @OneToMany(mappedBy = "jounal", fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnore
    private Set<JournalLine> journalLines = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReference() {
        return reference.toLowerCase();
    }

    public void setReference(String reference) {
        this.reference = reference.toUpperCase();
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    public LocalDateTime getTransDate() {
        return transDate;
    }

    public void setTransDate(LocalDateTime transDate) {
        this.transDate = transDate;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
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

    public Double getChanges() {

        return Double.valueOf(formatMoney(changes));
    }

    public void setChanges(Double changes) {
        this.changes = changes;
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

    public void setExternalRef(String externalref) {
        this.externalRef = externalref;
    }

    public TransactionStatus getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(TransactionStatus transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public Set<JournalLine> getJournalLines() {
        return journalLines;
    }

    public void setJournalLines(Set<JournalLine> journalLines) {
        this.journalLines = journalLines;
    }

    public String getDisplayMemo() {
        String externalRef = getExternalRef();
        if (narration != null && !narration.equalsIgnoreCase("null") && !narration.equalsIgnoreCase("")) {

            if (externalRef != null && !getExternalRef().equalsIgnoreCase("") && !getExternalRef().equalsIgnoreCase("null")) {
                return "R-" + getExternalRef() + "/" + narration;
            }
            return narration;
        }

        if (externalRef != null && !getExternalRef().equalsIgnoreCase("") && !getExternalRef().equalsIgnoreCase("null")) {
            return "R-" + getExternalRef() + "/" + getMemo().replaceAll("To Correspondent Account  ", " ").
                replaceAll(" From Correspondent Account ", " ");
        }

        return getMemo().replaceAll("To Correspondent Account  ", " ").
            replaceAll(" From Correspondent Account ", " ");

    }

    public double getAmount() {

        Set<JournalLine> journalLines = getJournalLines();

        List<Double> credits = new ArrayList<>();

        double commission = 0.0;

        if (journalLines != null) {
            for (JournalLine journalLine : journalLines) {
                if (journalLine.getCredit() > 0) {
                    WalletAccount walletAccount = journalLine.getWalletAccount();
                    if (walletAccount != null) {
                        if (!walletAccount.getAccountNumber().equalsIgnoreCase("1000000088")//Transit account
                            && !walletAccount.getAccountNumber().equalsIgnoreCase("1000000001")//Charge account
                            && !walletAccount.getAccountNumber().equalsIgnoreCase("1000000089")//Remita Income account
                        ) {
                            if (walletAccount.getAccountNumber().equalsIgnoreCase("1000000068")) {
                                commission = journalLine.getCredit();
                            }
                            credits.add(journalLine.getCredit());
                        }
                    }
                }
            }
        }

        Optional<Double> reduce = credits.stream().reduce(Double::max);
        if (reduce.isPresent()) {
            return reduce.get() + commission;
        } else {
            return 0.0;
        }

//        return reduce.orElse(0.0);


       /* OptionalDouble optionalDouble = journalLines.stream()
            .filter(p -> p.getCredit() > 0
                && !p.getWalletAccount().getAccountNumber().equalsIgnoreCase("1000000088")//Transit account
                && !p.getWalletAccount().getAccountNumber().equalsIgnoreCase("1000000001")//Charge account
                && !p.getWalletAccount().getAccountNumber().equalsIgnoreCase("1000000089"))//Remita Income account
            .mapToDouble(JournalLine::getCredit)
            .max();

      if (optionalDouble.isPresent()) {
            return optionalDouble.getAsDouble();
        }*/

    }

    public String getUserComment() {

        return userComment;

    }

    public void setUserComment(String userComment) {
        this.userComment = userComment;
    }

    @Override
    public String toString() {

        return "Journal{" +
            "id=" + id +
            ", changes=" + getChanges() +
            ", dueDate=" + dueDate +
            ", paymentType=" + paymentType +
            ", reference='" + reference + '\'' +
            ", memo='" + memo + '\'' +
            ", narration='" + narration + '\'' +
            ", externalRef='" + externalRef + '\'' +
            ", transDate=" + transDate +
            ", transactionStatus=" + transactionStatus +
            '}';

    }

    public static String formatMoney(Double amount) {
        if (amount == null) {
            return null;
        }

        return String.format("%.2f", amount);
    }

}
