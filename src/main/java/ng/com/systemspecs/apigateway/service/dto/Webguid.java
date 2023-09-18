package ng.com.systemspecs.apigateway.service.dto;

import com.fasterxml.jackson.annotation.*;

import javax.annotation.Generated;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "AmountDue",
    "CreditAccount",
    "webguid",
    "AgencyCode",
    "RevenueCode",
    "OraAgencyRev",
    "ReadOnly",
    "MinAmount",
    "MaxAmount",
    "Currency",
    "AcctCloseDate",
    "Order",
    "Flag_RO",
    "Flag_MinAmt",
    "Flag_MaxAmt"
})
@Generated("jsonschema2pojo")
public class Webguid {

    @JsonProperty("AmountDue")
    private Double amountDue;
    @JsonProperty("CreditAccount")
    private String creditAccount;
    @JsonProperty("webguid")
    private String webguid;
    @JsonProperty("AgencyCode")
    private String agencyCode;
    @JsonProperty("RevenueCode")
    private String revenueCode;
    @JsonProperty("OraAgencyRev")
    private String oraAgencyRev;
    @JsonProperty("ReadOnly")
    private Object readOnly;
    @JsonProperty("MinAmount")
    private Object minAmount;
    @JsonProperty("MaxAmount")
    private Object maxAmount;
    @JsonProperty("Currency")
    private String currency;
    @JsonProperty("AcctCloseDate")
    private String acctCloseDate;
    @JsonProperty("Order")
    private Integer order;
    @JsonProperty("Flag_RO")
    private String flagRO;
    @JsonProperty("Flag_MinAmt")
    private String flagMinAmt;
    @JsonProperty("Flag_MaxAmt")
    private String flagMaxAmt;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("AmountDue")
    public Double getAmountDue() {
        return amountDue;
    }

    @JsonProperty("AmountDue")
    public void setAmountDue(Double amountDue) {
        this.amountDue = amountDue;
    }

    @JsonProperty("CreditAccount")
    public String getCreditAccount() {
        return creditAccount;
    }

    @JsonProperty("CreditAccount")
    public void setCreditAccount(String creditAccount) {
        this.creditAccount = creditAccount;
    }

    @JsonProperty("webguid")
    public String getWebguid() {
        return webguid;
    }

    @JsonProperty("webguid")
    public void setWebguid(String webguid) {
        this.webguid = webguid;
    }

    @JsonProperty("AgencyCode")
    public String getAgencyCode() {
        return agencyCode;
    }

    @JsonProperty("AgencyCode")
    public void setAgencyCode(String agencyCode) {
        this.agencyCode = agencyCode;
    }

    @JsonProperty("RevenueCode")
    public String getRevenueCode() {
        return revenueCode;
    }

    @JsonProperty("RevenueCode")
    public void setRevenueCode(String revenueCode) {
        this.revenueCode = revenueCode;
    }

    @JsonProperty("OraAgencyRev")
    public String getOraAgencyRev() {
        return oraAgencyRev;
    }

    @JsonProperty("OraAgencyRev")
    public void setOraAgencyRev(String oraAgencyRev) {
        this.oraAgencyRev = oraAgencyRev;
    }

    @JsonProperty("ReadOnly")
    public Object getReadOnly() {
        return readOnly;
    }

    @JsonProperty("ReadOnly")
    public void setReadOnly(Object readOnly) {
        this.readOnly = readOnly;
    }

    @JsonProperty("MinAmount")
    public Object getMinAmount() {
        return minAmount;
    }

    @JsonProperty("MinAmount")
    public void setMinAmount(Object minAmount) {
        this.minAmount = minAmount;
    }

    @JsonProperty("MaxAmount")
    public Object getMaxAmount() {
        return maxAmount;
    }

    @JsonProperty("MaxAmount")
    public void setMaxAmount(Object maxAmount) {
        this.maxAmount = maxAmount;
    }

    @JsonProperty("Currency")
    public String getCurrency() {
        return currency;
    }

    @JsonProperty("Currency")
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @JsonProperty("AcctCloseDate")
    public String getAcctCloseDate() {
        return acctCloseDate;
    }

    @JsonProperty("AcctCloseDate")
    public void setAcctCloseDate(String acctCloseDate) {
        this.acctCloseDate = acctCloseDate;
    }

    @JsonProperty("Order")
    public Integer getOrder() {
        return order;
    }

    @JsonProperty("Order")
    public void setOrder(Integer order) {
        this.order = order;
    }

    @JsonProperty("Flag_RO")
    public String getFlagRO() {
        return flagRO;
    }

    @JsonProperty("Flag_RO")
    public void setFlagRO(String flagRO) {
        this.flagRO = flagRO;
    }

    @JsonProperty("Flag_MinAmt")
    public String getFlagMinAmt() {
        return flagMinAmt;
    }

    @JsonProperty("Flag_MinAmt")
    public void setFlagMinAmt(String flagMinAmt) {
        this.flagMinAmt = flagMinAmt;
    }

    @JsonProperty("Flag_MaxAmt")
    public String getFlagMaxAmt() {
        return flagMaxAmt;
    }

    @JsonProperty("Flag_MaxAmt")
    public void setFlagMaxAmt(String flagMaxAmt) {
        this.flagMaxAmt = flagMaxAmt;
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
        return "Webguid{" +
            "amountDue=" + amountDue +
            ", creditAccount='" + creditAccount + '\'' +
            ", webguid='" + webguid + '\'' +
            ", agencyCode='" + agencyCode + '\'' +
            ", revenueCode='" + revenueCode + '\'' +
            ", oraAgencyRev='" + oraAgencyRev + '\'' +
            ", readOnly=" + readOnly +
            ", minAmount=" + minAmount +
            ", maxAmount=" + maxAmount +
            ", currency='" + currency + '\'' +
            ", acctCloseDate='" + acctCloseDate + '\'' +
            ", order=" + order +
            ", flagRO='" + flagRO + '\'' +
            ", flagMinAmt='" + flagMinAmt + '\'' +
            ", flagMaxAmt='" + flagMaxAmt + '\'' +
            ", additionalProperties=" + additionalProperties +
            '}';
    }
}
