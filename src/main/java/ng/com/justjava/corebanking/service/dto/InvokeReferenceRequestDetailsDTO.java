package ng.com.justjava.corebanking.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.springframework.beans.factory.annotation.Value;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "TerminalId",
    "Channel",
    "Amount",
    "MerchantId",
    "TransactionType",
    "SubMerchantName",
    "TraceID"
})
public class InvokeReferenceRequestDetailsDTO {

    @Value("${app.coral.mid}")
    private String mid;

    @Value("${app.coral.tid}")
    private String tid;


    @JsonProperty("TerminalId")
    private String terminalId;
    @JsonProperty("Channel")
    private String channel = "USSD";
    @JsonProperty("Amount")
    private Double amount;
    @JsonProperty("MerchantId")
    private String merchantId;
    @JsonProperty("TransactionType")
    private String transactionType;
    @JsonProperty("SubMerchantName")
    private String subMerchantName = "Pouchii";
    @JsonProperty("TraceID")
    private String traceID;

    public InvokeReferenceRequestDetailsDTO() {
        this.channel = "USSD";
        this.merchantId = mid;
        this.terminalId = tid;
        this.subMerchantName = "DFS Wallet";
        this.transactionType = "0";
    }

    @JsonProperty("TerminalId")
    public String getTerminalId() {
        return terminalId;
    }

    @JsonProperty("TerminalId")
    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    @JsonProperty("Channel")
    public String getChannel() {
        return channel;
    }

    @JsonProperty("Channel")
    public void setChannel(String channel) {
        this.channel = channel;
    }

    @JsonProperty("Amount")
    public Double getAmount() {
        return amount;
    }

    @JsonProperty("Amount")
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @JsonProperty("MerchantId")
    public String getMerchantId() {
        return merchantId;
    }

    @JsonProperty("MerchantId")
    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    @JsonProperty("TransactionType")
    public String getTransactionType() {
        return transactionType;
    }

    @JsonProperty("TransactionType")
    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    @JsonProperty("SubMerchantName")
    public String getSubMerchantName() {
        return subMerchantName;
    }

    @JsonProperty("SubMerchantName")
    public void setSubMerchantName(String subMerchantName) {
        this.subMerchantName = subMerchantName;
    }

    @JsonProperty("TraceID")
    public String getTraceID() {
        return traceID;
    }

    @JsonProperty("TraceID")
    public void setTraceID(String traceID) {
        this.traceID = traceID;
    }

}
