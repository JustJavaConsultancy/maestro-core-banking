package ng.com.justjava.corebanking.service.dto;

import  ng.com.systemspecs.remitarits.bankenquiry.Banks;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class ResponseObject implements Serializable {

	private String  responseCode;
	private String responseMessage;
	private List<Banks>   data = new java.util.ArrayList<>();


	public String getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}


	public String getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	public List<Banks> getData() {
		return data;
	}

	public void setData(List<Banks> data) {
		this.data = data;
	}





}
