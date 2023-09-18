package ng.com.systemspecs.apigateway.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "status",
    "message",
    "data"
})
@Generated("jsonschema2pojo")
public class PolarisVulteResponseDTO {
    @JsonProperty("status")
    private String status;
    @JsonProperty("message")
    private String message;
    @JsonProperty("data")
    private PolarisVulteData data;
    @JsonProperty("provider_response")
    private Object providerResponse;

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    @JsonProperty("message")
    public void setMessage(String message) {
        this.message = message;
    }

    @JsonProperty("data")
    public PolarisVulteData getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(PolarisVulteData data) {
        this.data = data;
    }

    public Object getProviderResponse() {
        return providerResponse;
    }

    public void setProviderResponse(Object providerResponse) {
        this.providerResponse = providerResponse;
    }

    @Override
    public String toString() {
        return "PolarisVulteResponse{" +
            "status='" + status + '\'' +
            ", message='" + message + '\'' +
            ", data=" + data +
            ", providerResponse=" + providerResponse +
            '}';
    }
}
