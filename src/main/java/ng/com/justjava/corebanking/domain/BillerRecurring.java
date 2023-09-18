package ng.com.justjava.corebanking.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "biller_recurring")
public class BillerRecurring implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;
    @Column(name = "start")
    private LocalDate start;
    @Column(name = "next")
    private LocalDate next;
    @Column(name = "end")
    private LocalDate end;
    @Column(name = "retry")
    private LocalDate retry;
    @Column(name = "status")
    private String status;
    @Column(name = "success_count")
    private Integer successCount;
    @Column(name = "rrr")
    private String rrr;
    @Column(name = "amount_to_pay")
    private Double amountToPay;
    @Column(name = "account_number")
    private String accountNumber;
    @Column(name = "standing_debit")
    private String paymentFrequency;
    @Column(name = "number_of_times")
    private Integer numberOfTimes;
    @Column(name = "narration")
    private String narration;
    @Column(name = "redeem_bonus")
    private Boolean redeemBonus;
    @Column(name = "trans_ref")
    private String transRef;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStart() {
        return start;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public LocalDate getNext() {
        return next;
    }

    public void setNext(LocalDate next) {
        this.next = next;
    }

    public LocalDate getEnd() {
        return end;
    }

    public void setEnd(LocalDate end) {
        this.end = end;
    }

    public LocalDate getRetry() {
        return retry;
    }

    public void setRetry(LocalDate retry) {
        this.retry = retry;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(Integer successCount) {
        this.successCount = successCount;
    }

    public String getRrr() {
        return rrr;
    }

    public void setRrr(String rrr) {
        this.rrr = rrr;
    }

    public Double getAmountToPay() {
        return amountToPay;
    }

    public void setAmountToPay(Double amountToPay) {
        this.amountToPay = amountToPay;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getPaymentFrequency() {
        return paymentFrequency;
    }

    public void setPaymentFrequency(String paymentFrequency) {
        this.paymentFrequency = paymentFrequency;
    }

    public Integer getNumberOfTimes() {
        return numberOfTimes;
    }

    public void setNumberOfTimes(Integer numberOfTimes) {
        this.numberOfTimes = numberOfTimes;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public Boolean getRedeemBonus() {
        return redeemBonus;
    }

    public void setRedeemBonus(Boolean redeemBonus) {
        this.redeemBonus = redeemBonus;
    }

    public String getTransRef() {
        return transRef;
    }

    public void setTransRef(String transRef) {
        this.transRef = transRef;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "BillerRecurring{" +
            "id=" + id +
            ", start=" + start +
            ", next=" + next +
            ", end=" + end +
            ", retry=" + retry +
            ", status='" + status + '\'' +
            ", successCount=" + successCount +
            ", rrr='" + rrr + '\'' +
            ", amountToPay=" + amountToPay +
            ", accountNumber='" + accountNumber + '\'' +
            ", paymentFrequency='" + paymentFrequency + '\'' +
            ", numberOfTimes=" + numberOfTimes +
            ", narration='" + narration + '\'' +
            ", redeemBonus=" + redeemBonus +
            ", transRef='" + transRef + '\'' +
            ", user=" + user +
            '}';
    }
}
