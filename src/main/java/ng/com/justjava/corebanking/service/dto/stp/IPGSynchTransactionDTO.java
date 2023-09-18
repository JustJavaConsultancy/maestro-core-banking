package ng.com.justjava.corebanking.service.dto.stp;

import java.io.Serializable;
import java.math.BigDecimal;


//@JsonIgnoreProperties(value = {"TransactionType"})
public class IPGSynchTransactionDTO implements Serializable{

	private BigDecimal amount;
	private String channel;
	private String currency;
	private String sourceAccount;
	private String sourceAccountBankCode;
	private String sourceAccountName;
	private String sourceNarration;
	private String destinationAccount;
	private String destinationAccountBankCode;
	private String destinationAccountName;
	private String destinationNarration;
	private String transactionRef;

	private String transactionType;
	private BigDecimal feeAmount;
	private BigDecimal vatAmount;
	private String transactionAuthId;
	private String transactionSignature;
	private String partnerId;
	private BigDecimal totalAmountDebited;
	private String reversalRef;
	private Boolean reversed;

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
	@Override
	public String toString() {
		return "IPGSynchTransactionDTO [amount=" + amount + ", channel=" + channel + ", currency=" + currency
				+ ", sourceAccount=" + sourceAccount + ", sourceAccountBankCode=" + sourceAccountBankCode
				+ ", sourceAccountName=" + sourceAccountName + ", sourceNarration=" + sourceNarration
				+ ", destinationAccount=" + destinationAccount + ", destinationAccountBankCode="
				+ destinationAccountBankCode + ", destinationAccountName=" + destinationAccountName
				+ ", destinationNarration=" + destinationNarration + ", transactionRef=" + transactionRef
				+ ", transactionType=" + transactionType + ", feeAmount=" + feeAmount + ", vatAmount=" + vatAmount
				+ ", transactionAuthId=" + transactionAuthId + ", transactionSignature=" + transactionSignature
				+ ", partnerId=" + partnerId + ", totalAmountDebited=" + totalAmountDebited + ", reversalRef="
				+ reversalRef + ", reversed=" + reversed + "]";
	}

}
