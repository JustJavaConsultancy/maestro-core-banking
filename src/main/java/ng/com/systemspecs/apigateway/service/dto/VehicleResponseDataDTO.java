package ng.com.systemspecs.apigateway.service.dto;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

public class VehicleResponseDataDTO {
	@JsonProperty("policy_no")
	private String policyNo;
	@JsonProperty("certificate_no")
	private String certificateNo ;
	@JsonProperty("certificate_url")
	private String certificateUrl;
	@JsonProperty("balance")
	private Double balance;
	@JsonAnySetter
	private Map<String, Object> niid;
	
	@JsonProperty("policy_no")
	public String getPolicyNo() {
		return policyNo;
	}
	@JsonProperty("policy_no")
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}
	@JsonProperty("certificate_no")
	public String getCertificateNo() {
		return certificateNo;
	}
	@JsonProperty("certificate_no")
	public void setCertificateNo(String certificateNo) {
		this.certificateNo = certificateNo;
	}
	@JsonProperty("certificate_url")
	public String getCertificateUrl() {
		return certificateUrl;
	}
	@JsonProperty("certificate_url")
	public void setCertificateUrl(String certificateUrl) {
		this.certificateUrl = certificateUrl;
	}
	@JsonProperty("balance")
	public Double getBalance() {
		return balance;
	}
	
	@JsonProperty("balance")
	public void setBalance(Double balance) {
		this.balance = balance;
	}
	@JsonAnySetter
	public Map<String, Object> getNiid() {
		return niid;
	}
	@JsonAnySetter
	public void setNiid(Map<String, Object> niid) {
		this.niid = niid;
	}
	
	@Override
	public String toString() {
		return "vehicleResponseDataDTO [policyNo=" + policyNo + ", certificateNo=" + certificateNo + ", certificateUrl="
				+ certificateUrl + ", balance=" + balance + ", niid=" + niid + "]";
	}
}
