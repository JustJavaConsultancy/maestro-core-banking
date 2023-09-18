package ng.com.justjava.corebanking.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "statusCode",
    "body"
})
@Generated("jsonschema2pojo")
public class LoginResponse {

    @JsonProperty("statusCode")
    private Integer statusCode;
    @JsonProperty("body")
    private Body body;

    @JsonProperty("statusCode")
    public Integer getStatusCode() {
        return statusCode;
    }

    @JsonProperty("statusCode")
    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    @JsonProperty("body")
    public Body getBody() {
        return body;
    }

    @JsonProperty("body")
    public void setBody(Body body) {
        this.body = body;
    }


    @Override
    public String toString() {
        return "LoginResponse{" +
            "statusCode=" + statusCode +
            ", body=" + body +
            '}';
    }
}
