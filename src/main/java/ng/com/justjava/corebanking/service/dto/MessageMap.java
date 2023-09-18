package ng.com.justjava.corebanking.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "subject",
    "MAIL_PARAM_MAP",
    "toEmail",
    "productName"
})
public class MessageMap {

    @JsonProperty("subject")
    private String subject;
    @JsonProperty("MAIL_PARAM_MAP")
    private MAILPARAMMAP mAILPARAMMAP;
    @JsonProperty("toEmail")
    private String toEmail;
    @JsonProperty("productName")
    private String productName = "SystemSpecs Wallet";

    @JsonProperty("subject")
    public String getSubject() {
        return subject;
    }

    @JsonProperty("subject")
    public void setSubject(String subject) {
        this.subject = subject;
    }

    @JsonProperty("MAIL_PARAM_MAP")
    public MAILPARAMMAP getMAILPARAMMAP() {
        return mAILPARAMMAP;
    }

    @JsonProperty("MAIL_PARAM_MAP")
    public void setMAILPARAMMAP(MAILPARAMMAP mAILPARAMMAP) {
        this.mAILPARAMMAP = mAILPARAMMAP;
    }

    @JsonProperty("toEmail")
    public String getToEmail() {
        return toEmail;
    }

    @JsonProperty("toEmail")
    public void setToEmail(String toEmail) {
        this.toEmail = toEmail;
    }

    @JsonProperty("productName")
    public String getProductName() {
        return productName;
    }

    @JsonProperty("productName")
    public void setProductName(String productName) {
        this.productName = productName;
    }
}
