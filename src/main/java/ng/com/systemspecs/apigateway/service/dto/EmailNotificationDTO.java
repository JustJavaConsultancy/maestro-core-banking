package ng.com.systemspecs.apigateway.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.*;



public class EmailNotificationDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8199125559009502769L;

    private String title;
    
    private String sender;

    private String content;

    private List<AttachmentDTO> attachments = new ArrayList<AttachmentDTO>();
    
    private boolean isSent;
    
    private String customerName;
    
    private String customerPhone;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<AttachmentDTO> getAttachments() {
		return attachments;
	}

	public void setAttachments(ArrayList<AttachmentDTO> attachments) {
		this.attachments = attachments;
	}

	public boolean isSent() {
		return isSent;
	}

	public void setSent(boolean isSent) {
		this.isSent = isSent;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerPhone() {
		return customerPhone;
	}

	public void setCustomerPhone(String customerPhone) {
		this.customerPhone = customerPhone;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "EmailNotificationDTO {title:" + title + ", sender=:" + sender + ", content:" + content + ", attachments:"
				+ attachments + ", isSent:" + isSent + ", customerName:" + customerName + ", customerPhone:"
				+ customerPhone + "}";
	}    
}