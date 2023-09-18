package ng.com.systemspecs.apigateway.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "AccessToken",
    "ExpiresIn",
    "TokenType",
    "RefreshToken",
    "IdToken"
})
@Generated("jsonschema2pojo")
public class AuthenticationResult {

    @JsonProperty("AccessToken")
    private String accessToken;
    @JsonProperty("ExpiresIn")
    private Integer expiresIn;
    @JsonProperty("TokenType")
    private String tokenType;
    @JsonProperty("RefreshToken")
    private String refreshToken;
    @JsonProperty("IdToken")
    private String idToken;

    @JsonProperty("AccessToken")
    public String getAccessToken() {
        return accessToken;
    }

    @JsonProperty("AccessToken")
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @JsonProperty("ExpiresIn")
    public Integer getExpiresIn() {
        return expiresIn;
    }

    @JsonProperty("ExpiresIn")
    public void setExpiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
    }

    @JsonProperty("TokenType")
    public String getTokenType() {
        return tokenType;
    }

    @JsonProperty("TokenType")
    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    @JsonProperty("RefreshToken")
    public String getRefreshToken() {
        return refreshToken;
    }

    @JsonProperty("RefreshToken")
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    @JsonProperty("IdToken")
    public String getIdToken() {
        return idToken;
    }

    @JsonProperty("IdToken")
    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    @Override
    public String toString() {
        return "AuthenticationResult{" +
            "accessToken='" + accessToken + '\'' +
            ", expiresIn=" + expiresIn +
            ", tokenType='" + tokenType + '\'' +
            ", refreshToken='" + refreshToken + '\'' +
            ", idToken='" + idToken + '\'' +
            '}';
    }
}
