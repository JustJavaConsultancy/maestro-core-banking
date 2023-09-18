package ng.com.justjava.corebanking.service.dto;

import java.io.Serializable;

public class OTP  implements Serializable {


	private  String  smsMessage;
	private  String  mobileNumber;
	private  String  requestId;


	public String getSmsMessage() {
		return smsMessage;
	}
	public void setSmsMessage(String smsMessage) {
		this.smsMessage = smsMessage;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}




}
