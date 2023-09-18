package ng.com.systemspecs.apigateway.service.dto;


import com.fasterxml.jackson.annotation.*;

import javax.annotation.Generated;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "status",
    "data"
})
@Generated("jsonschema2pojo")
public class CashConnectWebHookDTO {

    @JsonProperty("status")
    private String status;
    @JsonProperty("data")
    private CashConnectWebHookDataDTO data;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("data")
    public CashConnectWebHookDataDTO getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(CashConnectWebHookDataDTO data) {
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

    @Override
    public String toString() {
        return "CashConnectWebHookDTO{" +
            "status='" + status + '\'' +
            ", data=" + data +
            ", additionalProperties=" + additionalProperties +
            '}';
    }
}
