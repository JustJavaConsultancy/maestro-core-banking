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
    "account",
    "amount",
    "smartCardCode"
})
@Generated("jsonschema2pojo")
public class ValidateStartimesDTO {

    @JsonProperty("service")
    private String service = "smile";
    @JsonProperty("channel")
    private String channel = "B2B";
    @JsonProperty("type")
    private String type = "default";
    @JsonProperty("amount")
    private String amount;
    @JsonProperty("smartCardCode")
    private String smartCardCode;

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

    @JsonProperty("amount")
    public String getAmount() {
        return amount;
    }

    @JsonProperty("amount")
    public void setAmount(String amount) {
        this.amount = amount;
    }

    @JsonProperty("smartCardCode")
    public String getSmartCardCode() {
        return smartCardCode;
    }

    @JsonProperty("smartCardCode")
    public void setSmartCardCode(String smartCardCode) {
        this.smartCardCode = smartCardCode;
    }

    @Override
    public String toString() {
        return "ValidateCableTvDTO{" +
            "service='" + service + '\'' +
            ", channel='" + channel + '\'' +
            ", type='" + type + '\'' +
            ", smartCardCode='" + smartCardCode + '\'' +
            '}';
    }
}
