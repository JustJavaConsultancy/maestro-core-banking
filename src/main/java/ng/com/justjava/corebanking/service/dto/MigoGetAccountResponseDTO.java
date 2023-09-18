package ng.com.justjava.corebanking.service.dto;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "bankName",
    "bankCode",
    "censoredAccountNo",
    "lastUsed"
})
@Generated("jsonschema2pojo")
public class MigoGetAccountResponseDTO {

    @JsonProperty("id")
    private String id;
    @JsonProperty("bankName")
    private String bankName;
    @JsonProperty("bankCode")
    private String bankCode;
    @JsonProperty("censoredAccountNo")
    private String censoredAccountNo;
    @JsonProperty("lastUsed")
    private String lastUsed;

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("bankName")
    public String getBankName() {
        return bankName;
    }

    @JsonProperty("bankName")
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    @JsonProperty("bankCode")
    public String getBankCode() {
        return bankCode;
    }

    @JsonProperty("bankCode")
    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    @JsonProperty("censoredAccountNo")
    public String getCensoredAccountNo() {
        return censoredAccountNo;
    }

    @JsonProperty("censoredAccountNo")
    public void setCensoredAccountNo(String censoredAccountNo) {
        this.censoredAccountNo = censoredAccountNo;
    }

    @JsonProperty("lastUsed")
    public String getLastUsed() {
        return lastUsed;
    }

    @JsonProperty("lastUsed")
    public void setLastUsed(String lastUsed) {
        this.lastUsed = lastUsed;
    }

    @Override
    public String toString() {
        return "MigoGetAccountResponseDTO{" +
            "id='" + id + '\'' +
            ", bankName='" + bankName + '\'' +
            ", bankCode='" + bankCode + '\'' +
            ", censoredAccountNo='" + censoredAccountNo + '\'' +
            ", lastUsed='" + lastUsed + '\'' +
            '}';
    }
}
