package ng.com.justjava.corebanking.service.dto.stp;

import java.io.Serializable;
import java.time.LocalDateTime;

public class StatementLine implements Serializable {

    private LocalDateTime transactionDate;
    private String narration;
    private String amount;
    private String currency;
    private String CRDR;

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCRDR() {
        return CRDR;
    }

    public void setCRDR(String CRDR) {
        this.CRDR = CRDR;
    }

    @Override
    public String toString() {
        return "StatementLine{" +
            "transactionDate=" + transactionDate +
            ", narration='" + narration + '\'' +
            ", amount='" + amount + '\'' +
            ", currency='" + currency + '\'' +
            ", CRDR='" + CRDR + '\'' +
            '}';
    }
}
