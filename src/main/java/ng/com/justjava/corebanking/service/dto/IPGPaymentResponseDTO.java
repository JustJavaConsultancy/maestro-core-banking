package ng.com.justjava.corebanking.service.dto;

import java.io.Serializable;

public class IPGPaymentResponseDTO implements Serializable {

	private String status;
	private String responseCode;
	private String responseMsg;
	private String iResponseCode;
	private String iResponseMessage;
	private String appVersionCode;
	private IPGPaymentResponseData[] responseData;
	private String  data;


	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	public String getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	public String getResponseMsg() {
		return responseMsg;
	}
	public IPGPaymentResponseData[] getResponseData() {
		return responseData;
	}

	public void setResponseData(IPGPaymentResponseData[] responseData) {
		this.responseData = responseData;
	}

	public void setResponseMsg(String responseMsg) {
		this.responseMsg = responseMsg;
	}
	public String getiResponseCode() {
		return iResponseCode;
	}
	public void setiResponseCode(String iResponseCode) {
		this.iResponseCode = iResponseCode;
	}
	public String getiResponseMessage() {
		return iResponseMessage;
	}
	public void setiResponseMessage(String iResponseMessage) {
		this.iResponseMessage = iResponseMessage;
	}
	public String getAppVersionCode() {
		return appVersionCode;
	}
	public void setAppVersionCode(String appVersionCode) {
		this.appVersionCode = appVersionCode;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}




}
