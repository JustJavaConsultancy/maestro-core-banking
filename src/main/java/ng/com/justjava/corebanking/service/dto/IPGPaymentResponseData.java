package ng.com.justjava.corebanking.service.dto;

import java.io.Serializable;

public class IPGPaymentResponseData  implements Serializable {

	private  String  rrr;
	private  String  amountDue;
	private  String  chargeFee;
	private  String  rrrAmount;

	private  String  payerEmail;
	private  String  payerName;
	private  String  payerPhone;
	private  String  description;
	private  String  currency;
	private  String  type;
	private  String  acceptPartPayment;
	private  String[]  extraData;
	private  String[]  customFields;


	public String getRrr() {
		return rrr;
	}
	public void setRrr(String rrr) {
		this.rrr = rrr;
	}
	public String getAmountDue() {
		return amountDue;
	}
	public void setAmountDue(String amountDue) {
		this.amountDue = amountDue;
	}
	public String getChargeFee() {
		return chargeFee;
	}
	public void setChargeFee(String chargeFee) {
		this.chargeFee = chargeFee;
	}
	public String getRrrAmount() {
		return rrrAmount;
	}
	public void setRrrAmount(String rrrAmount) {
		this.rrrAmount = rrrAmount;
	}
	public String getPayerEmail() {
		return payerEmail;
	}
	public void setPayerEmail(String payerEmail) {
		this.payerEmail = payerEmail;
	}
	public String getPayerName() {
		return payerName;
	}
	public void setPayerName(String payerName) {
		this.payerName = payerName;
	}
	public String getPayerPhone() {
		return payerPhone;
	}
	public void setPayerPhone(String payerPhone) {
		this.payerPhone = payerPhone;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getAcceptPartPayment() {
		return acceptPartPayment;
	}
	public void setAcceptPartPayment(String acceptPartPayment) {
		this.acceptPartPayment = acceptPartPayment;
	}
	public String[] getExtraData() {
		return extraData;
	}
	public void setExtraData(String[] extraData) {
		this.extraData = extraData;
	}
	public String[] getCustomFields() {
		return customFields;
	}
	public void setCustomFields(String[] customFields) {
		this.customFields = customFields;
	}



}
