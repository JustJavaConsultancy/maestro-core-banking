package ng.com.systemspecs.apigateway.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "message",
    "data"
})
@Generated("jsonschema2pojo")
public class Body {

    @JsonProperty("message")
    private String message;
    @JsonProperty("data")
    private LoginResponseData data;

    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    @JsonProperty("message")
    public void setMessage(String message) {
        this.message = message;
    }

    @JsonProperty("data")
    public LoginResponseData getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(LoginResponseData data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Body{" +
            "message='" + message + '\'' +
            ", data=" + data +
            '}';
    }
}
