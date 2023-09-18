package ng.com.justjava.corebanking.service.dto;

import java.io.Serializable;

public class AttachmentDTO implements Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = 4345943989425105716L;
	String encodedString;
	String docFormat;

	public String getEncodedString() {
		return encodedString;
	}
	public void setEncodedString(String encodedString) {
		this.encodedString = encodedString;
	}
	public String getDocFormat() {
		return docFormat;
	}
	public void setDocFormat(String docFormat) {
		this.docFormat = docFormat;
	}
	@Override
	public String toString() {
		return "{encodedString:" + encodedString + ", docFormat:" + docFormat + "}";
	}
}
