package ng.com.systemspecs.apigateway.service.dto; ;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "statusCode",
    "description",
    "data",
    "errors"
})
@Generated("jsonschema2pojo")
public class RemitaNINResponse {

    @JsonProperty("statusCode")
    private Integer statusCode;
    @JsonProperty("description")
    private String description;
    @JsonProperty("data")
    private RemitaNINResponseData data;
    @JsonProperty("errors")
    private Object errors;

    @JsonProperty("statusCode")
    public Integer getStatusCode() {
        return statusCode;
    }

    @JsonProperty("statusCode")
    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("data")
    public RemitaNINResponseData getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(RemitaNINResponseData data) {
        this.data = data;
    }

    @JsonProperty("errors")
    public Object getErrors() {
        return errors;
    }

    @JsonProperty("errors")
    public void setErrors(Object errors) {
        this.errors = errors;
    }

    @Override
    public String toString() {
        return "RemitaNINResponse{" +
            "statusCode=" + statusCode +
            ", description='" + description + '\'' +
            ", data=" + data +
            ", errors=" + errors +
            '}';
    }
}
