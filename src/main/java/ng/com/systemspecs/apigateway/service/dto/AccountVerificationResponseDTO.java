package ng.com.systemspecs.apigateway.service.dto;

public class AccountVerificationResponseDTO {
	private String accountNumber;
	private String accountName;
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
}
