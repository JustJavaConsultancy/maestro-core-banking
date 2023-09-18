package ng.com.justjava.corebanking.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "status",
    "data"
})
@Generated("jsonschema2pojo")
public class CashConnectResponse {

    @JsonProperty("status")
    private String status;
    @JsonProperty("data")
    private Object data;
    @JsonProperty("errors")
    private Object errors;

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("data")
    public Object getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(Object data) {
        this.data = data;
    }

    public Object getErrors() {
        return errors;
    }

    public void setErrors(Object errors) {
        this.errors = errors;
    }

    @Override
    public String toString() {
        return "CashConnectResponse{" +
            "status='" + status + '\'' +
            ", data=" + data +
            ", errors=" + errors +
            '}';
    }
}
