package ng.com.systemspecs.apigateway.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "provider_response_code",
    "provider",
    "errors",
    "error",
    "provider_response",
    "client_info"
})
@Generated("jsonschema2pojo")
public class PolarisVulteOpenAccountResponseData {

    @JsonProperty("provider_response_code")
    private String providerResponseCode;
    @JsonProperty("provider")
    private String provider;
    @JsonProperty("errors")
    private Object errors;
    @JsonProperty("error")
    private Object error;
    @JsonProperty("provider_response")
    private OpenAccountProviderResponse providerResponse;
    @JsonProperty("client_info")
    private ClientInfo clientInfo;

    @JsonProperty("provider_response_code")
    public String getProviderResponseCode() {
        return providerResponseCode;
    }

    @JsonProperty("provider_response_code")
    public void setProviderResponseCode(String providerResponseCode) {
        this.providerResponseCode = providerResponseCode;
    }

    @JsonProperty("provider")
    public String getProvider() {
        return provider;
    }

    @JsonProperty("provider")
    public void setProvider(String provider) {
        this.provider = provider;
    }

    @JsonProperty("errors")
    public Object getErrors() {
        return errors;
    }

    @JsonProperty("errors")
    public void setErrors(Object errors) {
        this.errors = errors;
    }

    @JsonProperty("error")
    public Object getError() {
        return error;
    }

    @JsonProperty("error")
    public void setError(Object error) {
        this.error = error;
    }

    @JsonProperty("provider_response")
    public OpenAccountProviderResponse getProviderResponse() {
        return providerResponse;
    }

    @JsonProperty("provider_response")
    public void setProviderResponse(OpenAccountProviderResponse providerResponse) {
        this.providerResponse = providerResponse;
    }

    @JsonProperty("client_info")
    public ClientInfo getClientInfo() {
        return clientInfo;
    }

    @JsonProperty("client_info")
    public void setClientInfo(ClientInfo clientInfo) {
        this.clientInfo = clientInfo;
    }

    @Override
    public String toString() {
        return "PolarisVulteOpenAccountResponseData{" +
            "providerResponseCode='" + providerResponseCode + '\'' +
            ", provider='" + provider + '\'' +
            ", errors=" + errors +
            ", error=" + error +
            ", providerResponse=" + providerResponse +
            ", clientInfo=" + clientInfo +
            '}';
    }
}
