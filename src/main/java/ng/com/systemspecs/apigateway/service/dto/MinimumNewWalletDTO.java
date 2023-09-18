package ng.com.systemspecs.apigateway.service.dto;

public class MinimumNewWalletDTO {
    private String accountName;
    private String phoneNumber;

    @Override
    public String toString() {
        return "MinimumNewWalletDTO [accountName=" + accountName + ", phoneNumber=" + phoneNumber
            + ", getAccountName()=" + getAccountName() + ", getPhoneNumber()=" + getPhoneNumber() + ", getClass()="
            + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
    }

    public String getAccountName() {
        return accountName;
	}

    public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

    public String getPhoneNumber() {
		return phoneNumber;
	}

    public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
}
