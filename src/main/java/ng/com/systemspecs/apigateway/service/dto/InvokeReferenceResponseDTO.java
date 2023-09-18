package ng.com.systemspecs.apigateway.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "ResponseHeader",
    "ResponseDetails"
})
public class InvokeReferenceResponseDTO {
    @JsonProperty("ResponseHeader")
    private CgatePaymentNotificationResponseDTO responseHeader;
    @JsonProperty("ResponseDetails")
    private InvokeReferenceResponseDetailsDTO responseDetails;

    @JsonProperty("ResponseHeader")
    public CgatePaymentNotificationResponseDTO getResponseHeader() {
        return responseHeader;
    }

    @JsonProperty("ResponseHeader")
    public void setResponseHeader(CgatePaymentNotificationResponseDTO responseHeader) {
        this.responseHeader = responseHeader;
    }

    @JsonProperty("ResponseDetails")
    public InvokeReferenceResponseDetailsDTO getResponseDetails() {
        return responseDetails;
    }

    @JsonProperty("ResponseDetails")
    public void setResponseDetails(InvokeReferenceResponseDetailsDTO responseDetails) {
        this.responseDetails = responseDetails;
    }

    @Override
    public String toString() {
        return "InvokeReferenceResponseDTO{" +
            "responseHeader=" + responseHeader +
            ", responseDetails=" + responseDetails +
            '}';
    }
}
