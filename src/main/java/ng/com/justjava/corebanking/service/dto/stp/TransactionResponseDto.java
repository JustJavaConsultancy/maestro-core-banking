package ng.com.justjava.corebanking.service.dto.stp;


import java.math.BigDecimal;


public class TransactionResponseDto extends BaseProcessedData {

    private BigDecimal amount;

    private String channel;

    private Currency currency;

    private String sourceAccount;

    private String sourceNarration;

    private String destinationAccount;

    private String destinationNarration;

    private String referenceId;

    private String authorizationRef;

    private String debitRef;

    private String creditRef;

    private String debitStatus;

    private String creditStatus;

    private BigDecimal debitAmount;

    private BigDecimal creditAmount;

    public String getDebitRef() {
        return debitRef;
    }

    public void setDebitRef(String debitRef) {
        this.debitRef = debitRef;
    }

    public String getCreditRef() {
        return creditRef;
    }

    public void setCreditRef(String creditRef) {
        this.creditRef = creditRef;
    }

    public String getDebitStatus() {
        return debitStatus;
    }

    public void setDebitStatus(String debitStatus) {
        this.debitStatus = debitStatus;
    }

    public String getCreditStatus() {
        return creditStatus;
    }

    public void setCreditStatus(String creditStatus) {
        this.creditStatus = creditStatus;
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


    public Currency getCurrency() {
        return currency;
    }


    public void setCurrency(Currency currency) {
        this.currency = currency;
    }


    public String getSourceAccount() {
        return sourceAccount;
    }


    public void setSourceAccount(String sourceAccount) {
        this.sourceAccount = sourceAccount;
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


    public String getDestinationNarration() {
        return destinationNarration;
    }


    public void setDestinationNarration(String destinationNarration) {
        this.destinationNarration = destinationNarration;
    }


    public String getReferenceId() {
        return referenceId;
    }


    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }


    public String getAuthorizationRef() {
        return authorizationRef;
    }


    public void setAuthorizationRef(String authorizationRef) {
        this.authorizationRef = authorizationRef;
    }

    public BigDecimal getDebitAmount() {
        return debitAmount;
    }

    public void setDebitAmount(BigDecimal debitAmount) {
        this.debitAmount = debitAmount;
    }

    public BigDecimal getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(BigDecimal creditAmount) {
        this.creditAmount = creditAmount;
    }
}
