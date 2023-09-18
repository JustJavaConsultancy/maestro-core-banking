package ng.com.justjava.corebanking.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "responseCode",
    "message",
    "data"
})
@Generated("jsonschema2pojo")
public class MultiChoiceSubscriptionResponseDTO {

    @JsonProperty("responseCode")
    private String responseCode;
    @JsonProperty("message")
    private String message;
    @JsonProperty("data")
    private MultiChoiceSubscriptionResponseDataDTO data;


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
    public MultiChoiceSubscriptionResponseDataDTO getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(MultiChoiceSubscriptionResponseDataDTO data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "MultiChoiceSubscriptionResponseDTO{" +
            "responseCode='" + responseCode + '\'' +
            ", message='" + message + '\'' +
            ", data=" + data +
            '}';
    }
}
