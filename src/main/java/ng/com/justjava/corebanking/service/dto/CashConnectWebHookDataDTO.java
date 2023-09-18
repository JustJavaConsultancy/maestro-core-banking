package ng.com.justjava.corebanking.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "beneficiary",
    "originator",
    "sessionId",
    "amount",
    "date"
})
@Generated("jsonschema2pojo")
public class CashConnectWebHookDataDTO {

    @JsonProperty("beneficiary")
    private CashConnectAccountDTO beneficiary;
    @JsonProperty("originator")
    private CashConnectAccountDTO originator;
    @JsonProperty("sessionId")
    private String sessionId;
    @JsonProperty("amount")
    private String amount;
    @JsonProperty("direction")
    private String direction;
    @JsonProperty("date")
    private String date;

    @JsonProperty("beneficiary")
    public CashConnectAccountDTO getBeneficiary() {
        return beneficiary;
    }

    @JsonProperty("beneficiary")
    public void setBeneficiary(CashConnectAccountDTO beneficiary) {
        this.beneficiary = beneficiary;
    }

    @JsonProperty("originator")
    public CashConnectAccountDTO getOriginator() {
        return originator;
    }

    @JsonProperty("originator")
    public void setOriginator(CashConnectAccountDTO originator) {
        this.originator = originator;
    }

    @JsonProperty("sessionId")
    public String getSessionId() {
        return sessionId;
    }

    @JsonProperty("sessionId")
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @JsonProperty("amount")
    public String getAmount() {
        return amount;
    }

    @JsonProperty("amount")
    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    @JsonProperty("date")
    public String getDate() {
        return date;
    }

    @JsonProperty("date")
    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "CashConnectWebHookDataDTO{" +
            "beneficiary=" + beneficiary +
            ", originator=" + originator +
            ", sessionId='" + sessionId + '\'' +
            ", amount='" + amount + '\'' +
            ", date='" + date + '\'' +
            ", direction='" + direction + '\'' +
            '}';
    }
}
