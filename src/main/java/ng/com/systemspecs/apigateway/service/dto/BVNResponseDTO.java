package ng.com.systemspecs.apigateway.service.dto;

import java.io.Serializable;

public class BVNResponseDTO implements Serializable {
	
	private String  responsedatetime;
	private String  responsecode;
	private String  lastName;
	private String  firstName;
	private String  phoneNumber;
	private BVNData  data;
	private String  base64Image;
	private String  responsemessage;
	private String  bvn;
	public String getResponsedatetime() {
		return responsedatetime;
	}
	public void setResponsedatetime(String responsedatetime) {
		this.responsedatetime = responsedatetime;
	}
	public String getResponsecode() {
		return responsecode;
	}
	public void setResponsecode(String responsecode) {
		this.responsecode = responsecode;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public BVNData getData() {
		return data;
	}
	public void setData(BVNData data) {
		this.data = data;
	}
	public String getBase64Image() {
		return base64Image;
	}
	public void setBase64Image(String base64Image) {
		this.base64Image = base64Image;
	}
	public String getResponsemessage() {
		return responsemessage;
	}
	public void setResponsemessage(String responsemessage) {
		this.responsemessage = responsemessage;
	}
	public String getBvn() {
		return bvn;
	}
	public void setBvn(String bvn) {
		this.bvn = bvn;
	}
	
	 
}
