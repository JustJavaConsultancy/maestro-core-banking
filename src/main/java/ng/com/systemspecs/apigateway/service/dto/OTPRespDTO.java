package ng.com.systemspecs.apigateway.service.dto;

import java.io.Serializable;

public class OTPRespDTO implements Serializable {

	
	private String  responseCode;
	private String   status;
	private String   responseMsg;
	private String   iResponseCode;
	private String   iResponseMessage;
	private String   appVersionCode;
	private String   data;
	
	
	private OTPResponseData[]   responseData; //  =  new OTPResponseData[1];
 
			
	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getResponseMsg() {
		return responseMsg;
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

	public OTPResponseData[] getResponseData() {
		return responseData;
	}

	public void setResponseData(OTPResponseData[] responseData) {
		this.responseData = responseData;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	
	
	
}
