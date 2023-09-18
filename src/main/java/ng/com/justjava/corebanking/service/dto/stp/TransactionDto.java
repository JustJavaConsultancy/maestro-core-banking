package ng.com.justjava.corebanking.service.dto.stp;


import ng.com.justjava.corebanking.domain.enumeration.stp.TransactionType;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;


public class TransactionDto {

    @NotNull(message = "Transaction amount not specified")
    @Min(value = 1, message = "Transaction amount must be greater than 0")
    private BigDecimal amount;

    private String channel;

    @NotNull(message = "Currency is required")
    private Currency currency;

    @NotNull(message = "Source account not specified")
    private String sourceAccount;

    // @NotNull(message = "Source account not specified")
    private String sourceAccountBankCode;


    private String sourceAccountName;

    @NotNull(message = "Transaction narration/description is required")
    private String sourceNarration;

    @NotNull(message = "Beneficiary/Destination account not specified")
    private String destinationAccount;

    private String destinationAccountBankCode;

    @NotNull
    private String destinationAccountName;

    private String destinationNarration;

    private String statusWebHook;


    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @NotNull(message = "A Unique transaction reference is required")
    private String referenceId;

    //    @NotNull(message = "Transaction PIN is required")
    private String transactionPin;

    @NotNull
    private String transactionAuthId;

    private String partnerId;

    @NotNull
    private String transactionAuthSignature;

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getTransactionAuthId() {
        return transactionAuthId;
    }

    public void setTransactionAuthId(String transactionAuthId) {
        this.transactionAuthId = transactionAuthId;
    }

    public String getTransactionAuthSignature() {
        return transactionAuthSignature;
    }

    public void setTransactionAuthSignature(String transactionAuthSignature) {
        this.transactionAuthSignature = transactionAuthSignature;
    }

    public String getSourceAccountBankCode() {
        return sourceAccountBankCode;
    }

    public void setSourceAccountBankCode(String sourceAccountBankCode) {
        this.sourceAccountBankCode = sourceAccountBankCode;
    }

    public String getDestinationAccountBankCode() {
        return destinationAccountBankCode;
    }

    public void setDestinationAccountBankCode(String destinationAccountBankCode) {
        this.destinationAccountBankCode = destinationAccountBankCode;
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


    public String getStatusWebHook() {
        return statusWebHook;
    }


    public void setStatusWebHook(String statusWebHook) {
        this.statusWebHook = statusWebHook;
    }


    public String getReferenceId() {
        return referenceId;
    }


    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }


    public String getTransactionPin() {
        return transactionPin;
    }


    public void setTransactionPin(String transactionPin) {
        this.transactionPin = transactionPin;
    }


    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }
}
