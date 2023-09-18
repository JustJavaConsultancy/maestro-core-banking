package ng.com.systemspecs.apigateway.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class InitiateBillerTransactionEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "recurring_id")
    private Long recurringId;

    @Column(name = "source_account")
    private String sourceAccount;

    @Column(name = "narration")
    private String narration;

    @Column(name = "redeem_bonus")
    private boolean redeemBonus;

    @Column(name = "bonus_amount")
    private Double bonusAmount;

    @Lob
    @Column(name = "initiate_biller_transaction")
    private String initiateBillerTransaction;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRecurringId() {
        return recurringId;
    }

    public void setRecurringId(Long recurringId) {
        this.recurringId = recurringId;
    }

    public String getSourceAccount() {
        return sourceAccount;
    }

    public void setSourceAccount(String sourceAccount) {
        this.sourceAccount = sourceAccount;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public boolean isRedeemBonus() {
        return redeemBonus;
    }

    public void setRedeemBonus(boolean redeemBonus) {
        this.redeemBonus = redeemBonus;
    }

    public Double getBonusAmount() {
        return bonusAmount;
    }

    public void setBonusAmount(Double bonusAmount) {
        this.bonusAmount = bonusAmount;
    }

    public String getInitiateBillerTransaction() {
        return initiateBillerTransaction;
    }

    public void setInitiateBillerTransaction(String initiateBillerTransaction) {
        this.initiateBillerTransaction = initiateBillerTransaction;
    }

    @Override
    public String toString() {
        return "InitiateBillerTransactionEntity{" +
            "id=" + id +
            ", userId=" + userId +
            ", recurringId=" + recurringId +
            ", sourceAccount='" + sourceAccount + '\'' +
            ", narration='" + narration + '\'' +
            ", redeemBonus=" + redeemBonus +
            ", bonusAmount=" + bonusAmount +
            ", initiateBillerTransaction='" + initiateBillerTransaction + '\'' +
            '}';
    }
}
