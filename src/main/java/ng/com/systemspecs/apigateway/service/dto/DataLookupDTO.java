package ng.com.systemspecs.apigateway.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "service",
    "channel"
})
@Generated("jsonschema2pojo")
public class DataLookupDTO {

    @JsonProperty("service")
    private String service;
    @JsonProperty("channel")
    private String channel;

    @JsonProperty("service")
    public String getService() {
        return service;
    }

    @JsonProperty("service")
    public void setService(String service) {
        this.service = service;
    }

    @JsonProperty("channel")
    public String getChannel() {
        return channel;
    }

    @JsonProperty("channel")
    public void setChannel(String channel) {
        this.channel = channel;
    }


    @Override
    public String toString() {
        return "DataLookupDTO{" +
            "service='" + service + '\'' +
            ", channel='" + channel + '\'' +
            '}';
    }
}
