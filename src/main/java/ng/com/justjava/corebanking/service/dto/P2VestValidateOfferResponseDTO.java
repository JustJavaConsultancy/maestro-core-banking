package ng.com.justjava.corebanking.service.dto;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "status",
    "message",
    "data"
})
@Generated("jsonschema2pojo")
public class P2VestValidateOfferResponseDTO {

    @JsonProperty("status")
    private String status;
    @JsonProperty("message")
    private String message;
    @JsonProperty("data")
    private P2VestValidateOfferResponseDataDTO data;

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    @JsonProperty("message")
    public void setMessage(String message) {
        this.message = message;
    }

    @JsonProperty("data")
    public P2VestValidateOfferResponseDataDTO getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(P2VestValidateOfferResponseDataDTO data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "P2VestValidateOfferResponseDTO{" +
            "status='" + status + '\'' +
            ", message='" + message + '\'' +
            ", data=" + data +
            '}';
    }
}
