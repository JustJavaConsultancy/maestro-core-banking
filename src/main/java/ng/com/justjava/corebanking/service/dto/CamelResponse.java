package ng.com.justjava.corebanking.service.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class CamelResponse implements Serializable {

    @JsonProperty("statusCode")
    private Integer statusCode;
    @JsonProperty("description")
    private String description;
    @JsonProperty("data")
    private Object data;
    @JsonProperty("errors")
    private Object errors;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

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
    public Object getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(PayBillerRRR data) {
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

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public String toString() {
        return "CamelResponse{" +
            "statusCode=" + statusCode +
            ", description='" + description + '\'' +
            ", data=" + data +
            ", errors=" + errors +
            ", additionalProperties=" + additionalProperties +
            '}';
    }
}
