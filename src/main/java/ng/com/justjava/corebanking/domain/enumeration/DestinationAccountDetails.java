package ng.com.justjava.corebanking.domain.enumeration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "destination_account",
    "destination_bank_code",
    "otp_override"
})
@Generated("jsonschema2pojo")
public class DestinationAccountDetails {

    @JsonProperty("destination_account")
    private String destinationAccount;
    @JsonProperty("destination_bank_code")
    private String destinationBankCode;
    @JsonProperty("otp_override")
    private Boolean otpOverride;

    @JsonProperty("destination_account")
    public String getDestinationAccount() {
        return destinationAccount;
    }

    @JsonProperty("destination_account")
    public void setDestinationAccount(String destinationAccount) {
        this.destinationAccount = destinationAccount;
    }

    @JsonProperty("destination_bank_code")
    public String getDestinationBankCode() {
        return destinationBankCode;
    }

    @JsonProperty("destination_bank_code")
    public void setDestinationBankCode(String destinationBankCode) {
        this.destinationBankCode = destinationBankCode;
    }

    @JsonProperty("otp_override")
    public Boolean getOtpOverride() {
        return otpOverride;
    }

    @JsonProperty("otp_override")
    public void setOtpOverride(Boolean otpOverride) {
        this.otpOverride = otpOverride;
    }

    @Override
    public String toString() {
        return "DestinationAccountDetails{" +
            "destinationAccount='" + destinationAccount + '\'' +
            ", destinationBankCode='" + destinationBankCode + '\'' +
            ", otpOverride=" + otpOverride +
            '}';
    }
}
