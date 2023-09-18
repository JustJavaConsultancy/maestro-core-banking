package ng.com.systemspecs.apigateway.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "request_ref",
    "request_type",
    "auth",
    "transaction"
})
@Generated("jsonschema2pojo")
public class PolarisVulteRequestDTO {
    @JsonProperty("request_ref")
    private String requestRef;
    @JsonProperty("request_type")
    private String requestType;
    @JsonProperty("auth")
    private PolarisVulteAuth auth;
    @JsonProperty("transaction")
    private PolarisVulteTransaction transaction;

    @JsonProperty("request_ref")
    public String getRequestRef() {
        return requestRef;
    }

    @JsonProperty("request_ref")
    public void setRequestRef(String requestRef) {
        this.requestRef = requestRef;
    }

    @JsonProperty("request_type")
    public String getRequestType() {
        return requestType;
    }

    @JsonProperty("request_type")
    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    @JsonProperty("auth")
    public PolarisVulteAuth getAuth() {
        return auth;
    }

    @JsonProperty("auth")
    public void setAuth(PolarisVulteAuth auth) {
        this.auth = auth;
    }

    @JsonProperty("transaction")
    public PolarisVulteTransaction getTransaction() {
        return transaction;
    }

    @JsonProperty("transaction")
    public void setTransaction(PolarisVulteTransaction transaction) {
        this.transaction = transaction;
    }

    @Override
    public String toString() {
        return "PolarisVulteRequestDTO{" +
            "requestRef='" + requestRef + '\'' +
            ", requestType='" + requestType + '\'' +
            ", auth=" + auth +
            ", transaction=" + transaction +
            '}';
    }
}
