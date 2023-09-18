package ng.com.systemspecs.apigateway.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "ResponseHeader",
    "ReversalDetails"
})
public class RefunResponseDTO {

    @JsonProperty("ResponseHeader")
    private CgatePaymentNotificationResponseDTO responseHeader;
    @JsonProperty("ReversalDetails")
    private ReversalDetailsDTO reversalDetails;

    @JsonProperty("ResponseHeader")
    public CgatePaymentNotificationResponseDTO getResponseHeader() {
        return responseHeader;
    }

    @JsonProperty("ResponseHeader")
    public void setResponseHeader(CgatePaymentNotificationResponseDTO responseHeader) {
        this.responseHeader = responseHeader;
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
        return "RefunResponseDTO{" +
            "responseHeader=" + responseHeader +
            ", reversalDetails=" + reversalDetails +
            '}';
    }
}
