package ng.com.justjava.corebanking.service.dto;

import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.annotation.JsonProperty;

public class DirectUSSDResponseDTO {
	private String response;
	private String freeflow;

	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	@JsonProperty("Freeflow")
	public String getFreeflow() {
		return freeflow;
	}
	public void setFreeflow(String freeflow) {
		this.freeflow = freeflow;
	}
	@Override
	public String toString() {
		return "DirectUSSDResponseDTO [response=" + response + ", freeflow=" + freeflow + "]";
	}

}
