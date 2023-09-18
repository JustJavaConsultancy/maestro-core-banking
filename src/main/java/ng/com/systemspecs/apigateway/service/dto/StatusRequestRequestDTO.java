package ng.com.systemspecs.apigateway.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "RequestHeaderDTO",
    "RequestDetailsDTO"
})
public class StatusRequestRequestDTO {
    @JsonProperty("RequestHeader")
    private RequestHeaderDTO requestHeader;
    @JsonProperty("RequestDetails")
    private RequestDetailsDTO requestDetails;

    @JsonProperty("RequestHeader")
    public RequestHeaderDTO getRequestHeader() {
        return requestHeader;
    }

    @JsonProperty("RequestHeader")
    public void setRequestHeader(RequestHeaderDTO requestHeader) {
        this.requestHeader = requestHeader;
    }

    @JsonProperty("RequestDetails")
    public RequestDetailsDTO getRequestDetails() {
        return requestDetails;
    }

    @JsonProperty("RequestDetails")
    public void setRequestDetails(RequestDetailsDTO requestDetails) {
        this.requestDetails = requestDetails;
    }

}
