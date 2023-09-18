package ng.com.justjava.corebanking.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "mailContent"
})
public class MAILPARAMMAP {

    @JsonProperty("mailContent")
    private String mailContent;

    public MAILPARAMMAP(String mailContent) {
        this.mailContent = mailContent;
    }

    @JsonProperty("mailContent")
    public String getMailContent() {
        return mailContent;
    }

    @JsonProperty("mailContent")
    public void setMailContent(String mailContent) {
        this.mailContent = mailContent;
    }
}
