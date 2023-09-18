package ng.com.systemspecs.apigateway.service.dto;

import java.io.Serializable;
import java.util.*;

import lombok.Data;
import ng.com.systemspecs.apigateway.domain.enumeration.KycRequestStatus;

public class KycRequestDecisionDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3987195280734867734L;
	Long kycRequestId;
	KycRequestStatus status;
	List<String> reason = new ArrayList<String>();
	public Long getKycRequestId() {
		return kycRequestId;
	}
	public void setKycRequestId(Long kycRequestId) {
		this.kycRequestId = kycRequestId;
	}
	public KycRequestStatus getStatus() {
		return status;
	}
	public void setStatus(KycRequestStatus status) {
		this.status = status;
	}
	public List<String> getReason() {
		return reason;
	}
	public void setReason(List<String> reason) {
		this.reason = reason;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Override
	public String toString() {
		return "KycRequestDecisionDTO = {kycRequestId:" + kycRequestId + ", status:" + status + ", reason:" + reason
				+ "]";
	}
	
	
}
