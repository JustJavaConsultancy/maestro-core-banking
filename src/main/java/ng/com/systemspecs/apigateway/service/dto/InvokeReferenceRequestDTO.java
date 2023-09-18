package ng.com.systemspecs.apigateway.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InvokeReferenceRequestDTO {

    @JsonProperty("RequestHeader")
    private RequestHeaderDTO requestHeader;
    @JsonProperty("RequestDetails")
    private InvokeReferenceRequestDetailsDTO requestDetails;

    @JsonProperty("RequestHeader")
    public RequestHeaderDTO getRequestHeader() {
        return requestHeader;
    }

    @JsonProperty("RequestHeader")
    public void setRequestHeader(RequestHeaderDTO requestHeader) {
        this.requestHeader = requestHeader;
    }

    @JsonProperty("RequestDetails")
    public InvokeReferenceRequestDetailsDTO getRequestDetails() {
        return requestDetails;
    }

    @JsonProperty("RequestDetails")
    public void setRequestDetails(InvokeReferenceRequestDetailsDTO requestDetails) {
        this.requestDetails = requestDetails;
    }

    @Override
    public String toString() {
        return "InvokeReferenceRequestDTO{" +
            "requestHeader=" + requestHeader +
            ", requestDetails=" + requestDetails +
            '}';
    }
}
