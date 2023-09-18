package ng.com.justjava.corebanking.service.dto;

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
    "approval",
    "error",
    "data"
})
@Generated("jsonschema2pojo")
public class ApproveLoanResponseDtoData {

    @JsonProperty("approval")
    private Boolean approval;
    @JsonProperty("error")
    private Object error;
    @JsonProperty("data")
    private ApproveLoanResponseData data;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("approval")
    public Boolean getApproval() {
        return approval;
    }

    @JsonProperty("approval")
    public void setApproval(Boolean approval) {
        this.approval = approval;
    }

    @JsonProperty("error")
    public Object getError() {
        return error;
    }

    @JsonProperty("error")
    public void setError(Object error) {
        this.error = error;
    }

    @JsonProperty("data")
    public ApproveLoanResponseData getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(ApproveLoanResponseData data) {
        this.data = data;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
