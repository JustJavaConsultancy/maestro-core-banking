package ng.com.systemspecs.apigateway.service.dto;

import com.fasterxml.jackson.annotation.*;

import javax.annotation.Generated;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "RequestId",
    "HTTPStatusCode",
    "HTTPHeaders",
    "RetryAttempts"
})
@Generated("jsonschema2pojo")
public class ResponseMetadata {

    @JsonProperty("RequestId")
    private String requestId;
    @JsonProperty("HTTPStatusCode")
    private Integer hTTPStatusCode;
    @JsonProperty("HTTPHeaders")
    private HTTPHeaders hTTPHeaders;
    @JsonProperty("RetryAttempts")
    private Integer retryAttempts;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("RequestId")
    public String getRequestId() {
        return requestId;
    }

    @JsonProperty("RequestId")
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    @JsonProperty("HTTPStatusCode")
    public Integer getHTTPStatusCode() {
        return hTTPStatusCode;
    }

    @JsonProperty("HTTPStatusCode")
    public void setHTTPStatusCode(Integer hTTPStatusCode) {
        this.hTTPStatusCode = hTTPStatusCode;
    }

    @JsonProperty("HTTPHeaders")
    public HTTPHeaders getHTTPHeaders() {
        return hTTPHeaders;
    }

    @JsonProperty("HTTPHeaders")
    public void setHTTPHeaders(HTTPHeaders hTTPHeaders) {
        this.hTTPHeaders = hTTPHeaders;
    }

    @JsonProperty("RetryAttempts")
    public Integer getRetryAttempts() {
        return retryAttempts;
    }

    @JsonProperty("RetryAttempts")
    public void setRetryAttempts(Integer retryAttempts) {
        this.retryAttempts = retryAttempts;
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
        return "ResponseMetadata{" +
            "requestId='" + requestId + '\'' +
            ", hTTPStatusCode=" + hTTPStatusCode +
            ", hTTPHeaders=" + hTTPHeaders +
            ", retryAttempts=" + retryAttempts +
            ", additionalProperties=" + additionalProperties +
            '}';
    }
}
