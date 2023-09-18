package ng.com.systemspecs.apigateway.service.dto;

import java.io.Serializable;

public class OTPResponseData  implements Serializable {
	 
   private String   lifespan;
   private String   creationTime;
   private String   passCode;
   private String   errorMessage;
   private String   sms;
   private String   message;
   private String   requestId;
   
   
public String getLifespan() {
	return lifespan;
}
public void setLifespan(String lifespan) {
	this.lifespan = lifespan;
}
public String getCreationTime() {
	return creationTime;
}
public void setCreationTime(String creationTime) {
	this.creationTime = creationTime;
}
public String getPassCode() {
	return passCode;
}
public void setPassCode(String passCode) {
	this.passCode = passCode;
}
public String getErrorMessage() {
	return errorMessage;
}
public void setErrorMessage(String errorMessage) {
	this.errorMessage = errorMessage;
}
public String getSms() {
	return sms;
}
public void setSms(String sms) {
	this.sms = sms;
}
public String getMessage() {
	return message;
}
public void setMessage(String message) {
	this.message = message;
}
public String getRequestId() {
	return requestId;
}
public void setRequestId(String requestId) {
	this.requestId = requestId;
}
   
   
   

}
