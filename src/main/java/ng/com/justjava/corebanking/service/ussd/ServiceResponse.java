package ng.com.justjava.corebanking.service.ussd;

public class ServiceResponse {
	private String content;

	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getMsgType() {
		return msgType;
	}
	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
	private String msgType = "1";
}
