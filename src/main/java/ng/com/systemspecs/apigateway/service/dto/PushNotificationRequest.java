package ng.com.systemspecs.apigateway.service.dto;


public class PushNotificationRequest {
    private String title;
    public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getTopic() {
		return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private String message;
    private String token;
    private String recipient;
    private String topic;

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    private String channel="";

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    @Override
    public String toString() {
        return "PushNotificationRequest{" +
            "title='" + title + '\'' +
            ", message='" + message + '\'' +
            ", token='" + token + '\'' +
            ", recipient='" + recipient + '\'' +
            ", topic='" + topic + '\'' +
            ", channel='" + channel + '\'' +
            '}';
    }
}
