package ng.com.systemspecs.apigateway.service.dto;

public class USSDRequestDTO {
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public String getMsisdn() {
		return msisdn;
	}
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
	public String getUssdContent() {
		return ussdContent;
	}
	public void setUssdContent(String ussdContent) {
		this.ussdContent = ussdContent;
	}
	public String getNetwork() {
		return network;
	}
	public void setNetwork(String network) {
		this.network = network;
	}
	private String timeStamp;
	private String sessionId;
	private String msisdn;
	private String ussdContent;
	private String network;
	
	@Override
	public String toString() {
		return "USSDRequestDTO [timeStamp=" + timeStamp + ", sessionId=" + sessionId + ", msisdn=" + msisdn
				+ ", ussdContent=" + ussdContent + ", network=" + network + "]";
	}

}
