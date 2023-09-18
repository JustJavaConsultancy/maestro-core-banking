package ng.com.justjava.corebanking.service.dto;

import com.fasterxml.jackson.annotation.*;

import javax.annotation.Generated;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "bulkBill"
})
@Generated("jsonschema2pojo")
public class ReferenceVerificationResponseDTO {

    @JsonProperty("bulkBill")
    private BulkBill bulkBill;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("bulkBill")
    public BulkBill getBulkBill() {
        return bulkBill;
    }

    @JsonProperty("bulkBill")
    public void setBulkBill(BulkBill bulkBill) {
        this.bulkBill = bulkBill;
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
        return "PaymentNotificationResponseDTO{" +
            "bulkBill=" + bulkBill +
            ", additionalProperties=" + additionalProperties +
            '}';
    }
}
