package ng.com.systemspecs.apigateway.service.dto;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "responseCode",
    "message",
    "data"
})
@Generated("jsonschema2pojo")
public class MeterValidationResponseDTO {

    @JsonProperty("responseCode")
    private String responseCode;
    @JsonProperty("message")
    private String message;
    @JsonProperty("data")
    private MeterValidationResponseDTOData data;

    @JsonProperty("responseCode")
    public String getResponseCode() {
        return responseCode;
    }

    @JsonProperty("responseCode")
    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
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
    public MeterValidationResponseDTOData getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(MeterValidationResponseDTOData data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "MeterValidationResponseDTO{" +
            "responseCode='" + responseCode + '\'' +
            ", message='" + message + '\'' +
            ", data=" + data +
            '}';
    }
}
