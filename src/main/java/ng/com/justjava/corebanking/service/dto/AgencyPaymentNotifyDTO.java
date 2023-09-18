package ng.com.justjava.corebanking.service.dto;

public class AgencyPaymentNotifyDTO {


	private String  rrr;
	private String  agentCode;
	private String  paymentAuthCode;
	private String  transactionId;
	private String  statusCode;
	private Double  amount = 0.00;


	public String getRrr() {
		return rrr;
	}
	public void setRrr(String rrr) {
		this.rrr = rrr;
	}
	public String getAgentCode() {
		return agentCode;
	}
	public void setAgentCode(String agentCode) {
		this.agentCode = agentCode;
	}
	public String getPaymentAuthCode() {
		return paymentAuthCode;
	}
	public void setPaymentAuthCode(String paymentAuthCode) {
		this.paymentAuthCode = paymentAuthCode;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}



}
