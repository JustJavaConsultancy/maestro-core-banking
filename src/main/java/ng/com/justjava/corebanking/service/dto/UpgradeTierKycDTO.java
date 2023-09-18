package ng.com.justjava.corebanking.service.dto;

import com.fasterxml.jackson.annotation.*;

import javax.annotation.Generated;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "accountNumber",
    "AccountTier",
    "NameOnUtilityBill",
    "AddressOnUtilityBill",
    "DateOnUtilityBill",
    "CommentsForUtilityBill"
})
@Generated("jsonschema2pojo")
public class UpgradeTierKycDTO {

    @JsonProperty("accountNumber")
    private String accountNumber;
    @JsonProperty("AccountTier")
    private Integer accountTier;
    @JsonProperty("NameOnUtilityBill")
    private String nameOnUtilityBill;
    @JsonProperty("AddressOnUtilityBill")
    private String addressOnUtilityBill;
    @JsonProperty("DateOnUtilityBill")
    private String dateOnUtilityBill;
    @JsonProperty("CommentsForUtilityBill")
    private String commentsForUtilityBill;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("accountNumber")
    public String getAccountNumber() {
        return accountNumber;
    }

    @JsonProperty("accountNumber")
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    @JsonProperty("AccountTier")
    public Integer getAccountTier() {
        return accountTier;
    }

    @JsonProperty("AccountTier")
    public void setAccountTier(Integer accountTier) {
        this.accountTier = accountTier;
    }

    @JsonProperty("NameOnUtilityBill")
    public String getNameOnUtilityBill() {
        return nameOnUtilityBill;
    }

    @JsonProperty("NameOnUtilityBill")
    public void setNameOnUtilityBill(String nameOnUtilityBill) {
        this.nameOnUtilityBill = nameOnUtilityBill;
    }

    @JsonProperty("AddressOnUtilityBill")
    public String getAddressOnUtilityBill() {
        return addressOnUtilityBill;
    }

    @JsonProperty("AddressOnUtilityBill")
    public void setAddressOnUtilityBill(String addressOnUtilityBill) {
        this.addressOnUtilityBill = addressOnUtilityBill;
    }

    @JsonProperty("DateOnUtilityBill")
    public String getDateOnUtilityBill() {
        return dateOnUtilityBill;
    }

    @JsonProperty("DateOnUtilityBill")
    public void setDateOnUtilityBill(String dateOnUtilityBill) {
        this.dateOnUtilityBill = dateOnUtilityBill;
    }

    @JsonProperty("CommentsForUtilityBill")
    public String getCommentsForUtilityBill() {
        return commentsForUtilityBill;
    }

    @JsonProperty("CommentsForUtilityBill")
    public void setCommentsForUtilityBill(String commentsForUtilityBill) {
        this.commentsForUtilityBill = commentsForUtilityBill;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public String toString() {
        return "UpgradeTierKycDTO{" +
            "accountNumber='" + accountNumber + '\'' +
            ", accountTier=" + accountTier +
            ", nameOnUtilityBill='" + nameOnUtilityBill + '\'' +
            ", addressOnUtilityBill='" + addressOnUtilityBill + '\'' +
            ", dateOnUtilityBill='" + dateOnUtilityBill + '\'' +
            ", commentsForUtilityBill='" + commentsForUtilityBill + '\'' +
            ", additionalProperties=" + additionalProperties +
            '}';
    }
}
