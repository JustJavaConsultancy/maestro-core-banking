package ng.com.systemspecs.apigateway.service.dto;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "clientNo",
    "offerId",
    "accountId"
})
@Generated("jsonschema2pojo")
public class MigoSubmitApplicationRequestDTO {

    @JsonProperty("clientNo")
    private String clientNo;
    @JsonProperty("offerId")
    private String offerId;
    @JsonProperty("accountId")
    private String accountId;

    @JsonProperty("clientNo")
    public String getClientNo() {
        return clientNo;
    }

    @JsonProperty("clientNo")
    public void setClientNo(String clientNo) {
        this.clientNo = clientNo;
    }

    @JsonProperty("offerId")
    public String getOfferId() {
        return offerId;
    }

    @JsonProperty("offerId")
    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    @JsonProperty("accountId")
    public String getAccountId() {
        return accountId;
    }

    @JsonProperty("accountId")
    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    @Override
    public String toString() {
        return "MigoSubmitApplicationResponseDTO{" +
            "clientNo='" + clientNo + '\'' +
            ", offerId='" + offerId + '\'' +
            ", accountId='" + accountId + '\'' +
            '}';
    }
}
