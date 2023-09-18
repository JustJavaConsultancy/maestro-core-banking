package ng.com.justjava.corebanking.service.dto;

public class MiniFundDTO {
	private String amount;
	private String walletID;
	private String transPin;
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getWalletID() {
		return walletID;
	}
	public void setWalletID(String walletID) {
		this.walletID = walletID;
	}

	@Override
	public String toString() {
		return "MiniFundDTO [amount=" + amount + ", "
				+ "walletID=" + walletID + ", getAmount()=" + getAmount()
				+ ", getWalletID()=" + getWalletID() + ", getClass()=" + getClass() +
				", hashCode()=" + hashCode()
				+ ", toString()=" + super.toString() + "]";
	}
	public String getTransPin() {
		return transPin;
	}
	public void setTransPin(String transPin) {
		this.transPin = transPin;
	}

}
