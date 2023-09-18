package ng.com.systemspecs.apigateway.service.dto;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "status",
	"data"
})
@Generated("jsonschema2pojo")
public class InsureVihicleResponseDTO {
	@JsonProperty("status")
	private boolean status;
	
	@JsonProperty("message")
	private String message;

	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	@JsonProperty("data")
	private VehicleResponseDataDTO data;
	
	@JsonProperty("status")
	public boolean isStatus() {
		return status;
	}
	@JsonProperty("status")
	public void setStatus(boolean status) {
		this.status = status;
	}
	@JsonProperty("data")
	public VehicleResponseDataDTO getData() {
		return data;
	}
	@JsonProperty("data")
	public void setData(VehicleResponseDataDTO data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return "InsureVihicleResponseDTO [status=" + status + ", message=" + message + ", data=" + data + "]";
	}
	
	

	
}