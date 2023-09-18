package ng.com.justjava.corebanking.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "RequestHeader",
    "ReversalDetails"
})
public class RefunRequestDTO {

    @JsonProperty("RequestHeader")
    private RequestHeaderDTO requestHeader;
    @JsonProperty("ReversalDetails")
    private ReversalDetailsDTO reversalDetails;


    @JsonProperty("RequestHeader")
    public RequestHeaderDTO getRequestHeader() {
        return requestHeader;
    }

    @JsonProperty("RequestHeader")
    public void setRequestHeader(RequestHeaderDTO requestHeader) {
        this.requestHeader = requestHeader;
    }

    @JsonProperty("ReversalDetails")
    public ReversalDetailsDTO getReversalDetails() {
        return reversalDetails;
    }

    @JsonProperty("ReversalDetails")
    public void setReversalDetails(ReversalDetailsDTO reversalDetails) {
        this.reversalDetails = reversalDetails;
    }

    @Override
    public String toString() {
        return "RefunRequestDTO{" +
            "requestHeader=" + requestHeader +
            ", reversalDetails=" + reversalDetails +
            '}';
    }
}
