package ng.com.systemspecs.apigateway.service.dto;

import java.io.Serializable;

public class TransferToAnotherBankDTO  implements  Serializable  {
	
    private  Double  amount;
    private  String  channel;
    private  String  currency;
    private  String  destinationAccount;
    private  String  destinationBankCode;
    private  String  destinationBankName;
    private  String  destinationAccountName;
    private  String  destinationNarration;
    
    private  String  endDate;
    private  String  referenceId;
    private  String  sourceAccount;
    private  String  sourceAccountName;
    private  String  sourceAmount;
    private  String  sourceCurrency;
    
    private  String  sourceNarration;
    private  String  startDate;
    private  String  transactionDate;
    private  String  statusWebHook;
    
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
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
	public String getDestinationAccount() {
		return destinationAccount;
	}
	public void setDestinationAccount(String destinationAccount) {
		this.destinationAccount = destinationAccount;
	}
	public String getDestinationBankCode() {
		return destinationBankCode;
	}
	public void setDestinationBankCode(String destinationBankCode) {
		this.destinationBankCode = destinationBankCode;
	}
	public String getDestinationBankName() {
		return destinationBankName;
	}
	public void setDestinationBankName(String destinationBankName) {
		this.destinationBankName = destinationBankName;
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
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getReferenceId() {
		return referenceId;
	}
	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
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
	public String getSourceAmount() {
		return sourceAmount;
	}
	public void setSourceAmount(String sourceAmount) {
		this.sourceAmount = sourceAmount;
	}
	public String getSourceCurrency() {
		return sourceCurrency;
	}
	public void setSourceCurrency(String sourceCurrency) {
		this.sourceCurrency = sourceCurrency;
	}
	public String getSourceNarration() {
		return sourceNarration;
	}
	public void setSourceNarration(String sourceNarration) {
		this.sourceNarration = sourceNarration;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}
	public String getStatusWebHook() {
		return statusWebHook;
	}
	public void setStatusWebHook(String statusWebHook) {
		this.statusWebHook = statusWebHook;
	} 
    
    
    
}
