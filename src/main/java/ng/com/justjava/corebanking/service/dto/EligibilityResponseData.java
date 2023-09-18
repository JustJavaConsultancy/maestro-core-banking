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
    "duration",
    "interest",
    "amount"
})
@Generated("jsonschema2pojo")
public class EligibilityResponseData {

    @JsonProperty("duration")
    private Integer duration;
    @JsonProperty("interest")
    private Integer interest;
    @JsonProperty("amount")
    private Integer amount;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("duration")
    public Integer getDuration() {
        return duration;
    }

    @JsonProperty("duration")
    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    @JsonProperty("interest")
    public Integer getInterest() {
        return interest;
    }

    @JsonProperty("interest")
    public void setInterest(Integer interest) {
        this.interest = interest;
    }

    @JsonProperty("amount")
    public Integer getAmount() {
        return amount;
    }

    @JsonProperty("amount")
    public void setAmount(Integer amount) {
        this.amount = amount;
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
