package ng.com.systemspecs.apigateway.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "provider_responde_code",
    "provider",
    "errors",
    "error",
    "charge_token",
    "paymentoptions"
})
@Generated("jsonschema2pojo")
public class PolarisVulteData {

    @JsonProperty("provider_responde_code")
    private String providerRespondeCode;
    @JsonProperty("provider")
    private String provider;
    @JsonProperty("errors")
    private List<Object> errors = null;
    @JsonProperty("error")
    private Object error;
    @JsonProperty("charge_token")
    private String chargeToken;
    @JsonProperty("provider_response")
    private Object providerResponse;
    @JsonProperty("client_info")
    private ClientInfo clientInfo;
    @JsonProperty("paymentoptions")
    private List<Object> paymentoptions = null;

    @JsonProperty("provider_responde_code")
    public String getProviderRespondeCode() {
        return providerRespondeCode;
    }

    @JsonProperty("provider_responde_code")
    public void setProviderRespondeCode(String providerRespondeCode) {
        this.providerRespondeCode = providerRespondeCode;
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
    public List<Object> getErrors() {
        return errors;
    }

    @JsonProperty("errors")
    public void setErrors(List<Object> errors) {
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

    @JsonProperty("charge_token")
    public String getChargeToken() {
        return chargeToken;
    }

    @JsonProperty("charge_token")
    public void setChargeToken(String chargeToken) {
        this.chargeToken = chargeToken;
    }

    @JsonProperty("paymentoptions")
    public List<Object> getPaymentoptions() {
        return paymentoptions;
    }

    @JsonProperty("paymentoptions")
    public void setPaymentoptions(List<Object> paymentoptions) {
        this.paymentoptions = paymentoptions;
    }

    public Object getProviderResponse() {
        return providerResponse;
    }

    public void setProviderResponse(Object providerResponse) {
        this.providerResponse = providerResponse;
    }

    public ClientInfo getClientInfo() {
        return clientInfo;
    }

    public void setClientInfo(ClientInfo clientInfo) {
        this.clientInfo = clientInfo;
    }

    @Override
    public String toString() {
        return "PolarisVulteData{" +
            "providerRespondeCode='" + providerRespondeCode + '\'' +
            ", provider='" + provider + '\'' +
            ", errors=" + errors +
            ", error=" + error +
            ", chargeToken='" + chargeToken + '\'' +
            ", paymentoptions=" + paymentoptions +
            ", providerResponse=" + providerResponse +
            ", clientInfo=" + clientInfo +
            '}';
    }
}

