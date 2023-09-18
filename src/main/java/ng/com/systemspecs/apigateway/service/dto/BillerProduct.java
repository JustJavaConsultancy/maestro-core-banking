package ng.com.systemspecs.apigateway.service.dto;

import com.fasterxml.jackson.annotation.*;

import javax.annotation.Generated;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "serviceName",
    "serviceId",
    "amount",
    "customFields"
})
@Generated("jsonschema2pojo")
public class BillerProduct implements Serializable {

    @JsonProperty("serviceName")
    private String serviceName;
    @JsonProperty("serviceId")
    private String serviceId;
    @JsonProperty("amount")
    private Double amount;
    @JsonProperty("customFields")
    private List<BillerProductCustomField> customFields = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("serviceName")
    public String getServiceName() {
        return serviceName;
    }

    @JsonProperty("serviceName")
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    @JsonProperty("serviceId")
    public String getServiceId() {
        return serviceId;
    }

    @JsonProperty("serviceId")
    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    @JsonProperty("amount")
    public Double getAmount() {
        return amount;
    }

    @JsonProperty("amount")
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @JsonProperty("customFields")
    public List<BillerProductCustomField> getCustomFields() {
        return customFields;
    }

    @JsonProperty("customFields")
    public void setCustomFields(List<BillerProductCustomField> customFields) {
        this.customFields = customFields;
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
        return "BillerProduct{" +
            "serviceName='" + serviceName + '\'' +
            ", serviceId='" + serviceId + '\'' +
            ", amount=" + amount +
            ", customFields=" + customFields +
            ", additionalProperties=" + additionalProperties +
            '}';
    }
}
