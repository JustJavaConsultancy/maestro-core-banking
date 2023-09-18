package ng.com.systemspecs.apigateway.service.dto;

import com.fasterxml.jackson.annotation.*;

import javax.annotation.Generated;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "ChallengeParameters",
    "AuthenticationResult",
    "ResponseMetadata"
})
@Generated("jsonschema2pojo")
public class LoginResponseData {

    @JsonProperty("ChallengeParameters")
    private ChallengeParameters challengeParameters;
    @JsonProperty("AuthenticationResult")
    private AuthenticationResult authenticationResult;
    @JsonProperty("ResponseMetadata")
    private ResponseMetadata responseMetadata;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("ChallengeParameters")
    public ChallengeParameters getChallengeParameters() {
        return challengeParameters;
    }

    @JsonProperty("ChallengeParameters")
    public void setChallengeParameters(ChallengeParameters challengeParameters) {
        this.challengeParameters = challengeParameters;
    }

    @JsonProperty("AuthenticationResult")
    public AuthenticationResult getAuthenticationResult() {
        return authenticationResult;
    }

    @JsonProperty("AuthenticationResult")
    public void setAuthenticationResult(AuthenticationResult authenticationResult) {
        this.authenticationResult = authenticationResult;
    }

    @JsonProperty("ResponseMetadata")
    public ResponseMetadata getResponseMetadata() {
        return responseMetadata;
    }

    @JsonProperty("ResponseMetadata")
    public void setResponseMetadata(ResponseMetadata responseMetadata) {
        this.responseMetadata = responseMetadata;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public String toString() {
        return "LoginResponseData{" +
            "challengeParameters=" + challengeParameters +
            ", authenticationResult=" + authenticationResult +
            ", responseMetadata=" + responseMetadata +
            ", additionalProperties=" + additionalProperties +
            '}';
    }
}
