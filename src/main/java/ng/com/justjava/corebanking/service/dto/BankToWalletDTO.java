package ng.com.justjava.corebanking.service.dto;

import java.io.Serializable;

public class BankToWalletDTO implements  Serializable  {

	  private  String  fromAccount;
      private  String  fromBank;
      private  String  toAccount;
      private  String  account;
      private  String  narration;
      private  String  paymentRef;


	public String getFromAccount() {
		return fromAccount;
	}

	public void setFromAccount(String fromAccount) {
		this.fromAccount = fromAccount;
	}

	public String getFromBank() {
		return fromBank;
	}

	public void setFromBank(String fromBank) {
		this.fromBank = fromBank;
	}

	public String getToAccount() {
		return toAccount;
	}

	public void setToAccount(String toAccount) {
		this.toAccount = toAccount;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getNarration() {
		return narration;
	}

	public void setNarration(String narration) {
		this.narration = narration;
	}

	public String getPaymentRef() {
		return paymentRef;
	}


	public void setPaymentRef(String paymentRef) {
		this.paymentRef = paymentRef;
	}


}
