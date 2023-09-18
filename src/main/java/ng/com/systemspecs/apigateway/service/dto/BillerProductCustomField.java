
package ng.com.systemspecs.apigateway.service.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
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
    "displayName",
    "variableName",
    "type",
    "required",
    "selectOptions"
})
@Generated("jsonschema2pojo")
public class BillerProductCustomField implements Serializable {

    @JsonProperty("displayName")
    private String displayName;
    @JsonProperty("variableName")
    private String variableName;
    @JsonProperty("type")
    private String type;
    @JsonProperty("required")
    private Boolean required;
    @JsonProperty("selectOptions")
    private List<BillerSelectOptions> selectOptions = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("displayName")
    public String getDisplayName() {
        return displayName;
    }

    @JsonProperty("displayName")
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @JsonProperty("variableName")
    public String getVariableName() {
        return variableName;
    }

    @JsonProperty("variableName")
    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("required")
    public Boolean getRequired() {
        return required;
    }

    @JsonProperty("required")
    public void setRequired(Boolean required) {
        this.required = required;
    }

    @JsonProperty("selectOptions")
    public List<BillerSelectOptions> getSelectOptions() {
        return selectOptions;
    }

    @JsonProperty("selectOptions")
    public void setSelectOptions(List<BillerSelectOptions> selectOptions) {
        this.selectOptions = selectOptions;
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
        return "BillerProductCustomField{" +
            "displayName='" + displayName + '\'' +
            ", variableName='" + variableName + '\'' +
            ", type='" + type + '\'' +
            ", required=" + required +
            ", selectOptions=" + selectOptions +
            ", additionalProperties=" + additionalProperties +
            '}';
    }
}
