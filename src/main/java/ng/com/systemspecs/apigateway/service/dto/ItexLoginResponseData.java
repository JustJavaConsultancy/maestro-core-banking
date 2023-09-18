package ng.com.systemspecs.apigateway.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "responseCode",
    "apiToken",
    "expiration",
    "apiUser"
})
@Generated("princely")
public class ItexLoginResponseData {

    @JsonProperty("responseCode")
    private String responseCode;
    @JsonProperty("apiToken")
    private String apiToken;
    @JsonProperty("expiration")
    private String expiration;
    @JsonProperty("apiUser")
    private String apiUser;

    @JsonProperty("responseCode")
    public String getResponseCode() {
        return responseCode;
    }

    @JsonProperty("responseCode")
    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    @JsonProperty("apiToken")
    public String getApiToken() {
        return apiToken;
    }

    @JsonProperty("apiToken")
    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }

    @JsonProperty("expiration")
    public String getExpiration() {
        return expiration;
    }

    @JsonProperty("expiration")
    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    @JsonProperty("apiUser")
    public String getApiUser() {
        return apiUser;
    }

    @JsonProperty("apiUser")
    public void setApiUser(String apiUser) {
        this.apiUser = apiUser;
    }

    @Override
    public String toString() {
        return "ItexLoginResponseData{" +
            "responseCode='" + responseCode + '\'' +
            ", apiToken='" + apiToken + '\'' +
            ", expiration='" + expiration + '\'' +
            ", apiUser='" + apiUser + '\'' +
            '}';
    }
}
