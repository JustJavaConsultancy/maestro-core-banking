package ng.com.justjava.corebanking.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "messageMap"
})
public class SendEmailDTO {

    @JsonProperty("messageMap")
    private MessageMap messageMap;

    public SendEmailDTO(MessageMap messageMap) {
        this.messageMap = messageMap;
    }

    @JsonProperty("messageMap")
    public MessageMap getMessageMap() {
        return messageMap;
    }

    @JsonProperty("messageMap")
    public void setMessageMap(MessageMap messageMap) {
        this.messageMap = messageMap;
    }

}
