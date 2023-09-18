package ng.com.justjava.corebanking.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "service",
    "type"
})
@Generated("jsonschema2pojo")
public class BouquetRequestDTO {

    @JsonProperty("service")
    private String service;
    @JsonProperty("type")
    private String type;

    @JsonProperty("service")
    public String getService() {
        return service;
    }

    @JsonProperty("service")
    public void setService(String service) {
        this.service = service;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }


    @Override
    public String toString() {
        return "BouquetRequestDTO{" +
            "service='" + service + '\'' +
            ", type='" + type + '\'' +
            '}';
    }
}
