package ng.com.justjava.corebanking.service.dto;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "meterNo",
    "accountType",
    "searchCode",
    "service",
    "amount",
    "channel"
})
@Generated("jsonschema2pojo")
public class MeterValidationDTO {

    @JsonProperty("meterNo")
    private String meterNo;
    @JsonProperty("accountType")
    private String accountType;
    @JsonProperty("searchCode")
    private String searchCode = "MY003";
    @JsonProperty("service")
    private String service;
    @JsonProperty("amount")
    private String amount;
    @JsonProperty("channel")
    private String channel = "B2B";

    @JsonProperty("meterNo")
    public String getMeterNo() {
        return meterNo;
    }

    @JsonProperty("meterNo")
    public void setMeterNo(String meterNo) {
        this.meterNo = meterNo;
    }

    @JsonProperty("accountType")
    public String getAccountType() {
        return accountType;
    }

    @JsonProperty("accountType")
    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    @JsonProperty("searchCode")
    public String getSearchCode() {
        return searchCode;
    }

    @JsonProperty("searchCode")
    public void setSearchCode(String searchCode) {
        this.searchCode = searchCode;
    }

    @JsonProperty("service")
    public String getService() {
        return service;
    }

    @JsonProperty("service")
    public void setService(String service) {
        this.service = service;
    }

    @JsonProperty("amount")
    public String getAmount() {
        return amount;
    }

    @JsonProperty("amount")
    public void setAmount(String amount) {
        this.amount = amount;
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
        return "MeterValidationDTO{" +
            "meterNo='" + meterNo + '\'' +
            ", accountType='" + accountType + '\'' +
            ", searchCode='" + searchCode + '\'' +
            ", service='" + service + '\'' +
            ", amount='" + amount + '\'' +
            ", channel='" + channel + '\'' +
            '}';
    }
}
