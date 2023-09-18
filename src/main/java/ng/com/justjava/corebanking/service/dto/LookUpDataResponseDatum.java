package ng.com.justjava.corebanking.service.dto;

import com.fasterxml.jackson.annotation.*;

import javax.annotation.Generated;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "type",
    "tariff_id",
    "code",
    "duration",
    "amount",
    "value",
    "description"
})
@Generated("jsonschema2pojo")
public class LookUpDataResponseDatum {

    @JsonProperty("type")
    private String type;
    @JsonProperty("tariff_id")
    private String tariffId;
    @JsonProperty("code")
    private String code;
    @JsonProperty("duration")
    private String duration;
    @JsonProperty("amount")
    private String amount;
    @JsonProperty("value")
    private String value;
    @JsonProperty("description")
    private String description;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("tariff_id")
    public String getTariffId() {
        return tariffId;
    }

    @JsonProperty("tariff_id")
    public void setTariffId(String tariffId) {
        this.tariffId = tariffId;
    }

    @JsonProperty("code")
    public String getCode() {
        return code;
    }

    @JsonProperty("code")
    public void setCode(String code) {
        this.code = code;
    }

    @JsonProperty("duration")
    public String getDuration() {
        return duration;
    }

    @JsonProperty("duration")
    public void setDuration(String duration) {
        this.duration = duration;
    }

    @JsonProperty("amount")
    public String getAmount() {
        return amount;
    }

    @JsonProperty("amount")
    public void setAmount(String amount) {
        this.amount = amount;
    }

    @JsonProperty("value")
    public String getValue() {
        return value;
    }

    @JsonProperty("value")
    public void setValue(String value) {
        this.value = value;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
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
        return "LookUpDataResponseDatum{" +
            "type='" + type + '\'' +
            ", tariffId='" + tariffId + '\'' +
            ", code='" + code + '\'' +
            ", duration='" + duration + '\'' +
            ", amount='" + amount + '\'' +
            ", value='" + value + '\'' +
            ", description='" + description + '\'' +
            ", additionalProperties=" + additionalProperties +
            '}';
    }
}
