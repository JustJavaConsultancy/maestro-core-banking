package ng.com.systemspecs.apigateway.service.dto;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "service"
})
@Generated("jsonschema2pojo")
public class ItexSearchRequestDTO {

    @JsonProperty("service")
    private String service = "aedc";

    @JsonProperty("service")
    public String getService() {
        return service;
    }

    @JsonProperty("service")
    public void setService(String service) {
        this.service = service;
    }

    @Override
    public String toString() {
        return "ItexSearchRequestDTO{" +
            "service='" + service + '\'' +
            '}';
    }
}
