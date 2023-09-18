package ng.com.systemspecs.apigateway.service.dto;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "transactionId",
    "timestamp",
    "channel"
})
@Generated("jsonschema2pojo")
public class MigoPaymentMethodDTO {

    @JsonProperty("transactionId")
    private String transactionId;
    @JsonProperty("timestamp")
    private String timestamp;
    @JsonProperty("channel")
    private String channel;

    @JsonProperty("transactionId")
    public String getTransactionId() {
        return transactionId;
    }

    @JsonProperty("transactionId")
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    @JsonProperty("timestamp")
    public String getTimestamp() {
        return timestamp;
    }

    @JsonProperty("timestamp")
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
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
        return "MigoPaymentMethod{" +
            "transactionId='" + transactionId + '\'' +
            ", timestamp='" + timestamp + '\'' +
            ", channel='" + channel + '\'' +
            '}';
    }
}
