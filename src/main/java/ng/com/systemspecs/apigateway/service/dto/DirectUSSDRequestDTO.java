package ng.com.systemspecs.apigateway.service.dto;

public class DirectUSSDRequestDTO {
	
private String datetime;
private String msisdn;
private String type;
private String input;
private String sessionid;
private String network;

public String getDatetime() {
	return datetime;
}
public void setDatetime(String datetime) {
	this.datetime = datetime;
}
public String getMsisdn() {
	return msisdn;
}
public void setMsisdn(String msisdn) {
	this.msisdn = msisdn;
}
public String getType() {
	return type;
}
public void setType(String type) {
	this.type = type;
}
public String getInput() {
	return input;
}
public void setInput(String input) {
	this.input = input;
}
public String getSessionid() {
	return sessionid;
}
public void setSessionid(String sessionid) {
	this.sessionid = sessionid;
}
public String getNetwork() {
	return network;
}
public void setNetwork(String network) {
	this.network = network;
}
@Override
public String toString() {
	return "DirectUSSDRequestDTO [datetime=" + datetime + ", msisdn=" + msisdn + ", type=" + type + ", input=" + input
			+ ", sessionid=" + sessionid + ", network=" + network + "]";
}

}
