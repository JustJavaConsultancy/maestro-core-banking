package ng.com.systemspecs.apigateway.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "service",
    "channel",
    "type",
    "account"
})
@Generated("jsonschema2pojo")
public class InternetValidationDTO {

    @JsonProperty("service")
    private String service = "smile";
    @JsonProperty("channel")
    private String channel = "B2B";
    @JsonProperty("type")
    private String type = "account";
    @JsonProperty("account")
    private String account;

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

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("account")
    public String getAccount() {
        return account;
    }

    @JsonProperty("account")
    public void setAccount(String account) {
        this.account = account;
    }


    @Override
    public String toString() {
        return "InternetValidationDTO{" +
            "service='" + service + '\'' +
            ", channel='" + channel + '\'' +
            ", type='" + type + '\'' +
            ", account='" + account + '\'' +
            '}';
    }
}
