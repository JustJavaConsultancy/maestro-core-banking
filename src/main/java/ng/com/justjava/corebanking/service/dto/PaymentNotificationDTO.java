package ng.com.justjava.corebanking.service.dto;

import com.fasterxml.jackson.annotation.*;

import javax.annotation.Generated;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "Webguid",
    "AmountPaid",
    "PaymentRef",
    "CreditAccount",
    "Date",
    "Hash",
    "State",
    "ClientId",
    "PaymentChannel",
    "TellerName",
    "SendBy",
    "TellerID",
    "BankNote",
    "CbnCode",
    "BranchName",
    "PosId",
    "TransId",
    "TransRef",
    "TerminalId"
})
@Generated("jsonschema2pojo")
public class PaymentNotificationDTO {

    @JsonProperty("Webguid")
    private String webguid;
    @JsonProperty("AmountPaid")
    private String amountPaid;
    @JsonProperty("PaymentRef")
    private String paymentRef;
    @JsonProperty("CreditAccount")
    private String creditAccount;
    @JsonProperty("Date")
    private String date;
    @JsonProperty("Hash")
    private String hash;
    @JsonProperty("State")
    private String state;
    @JsonProperty("ClientId")
    private String clientId;
    @JsonProperty("PaymentChannel")
    private String paymentChannel;
    @JsonProperty("TellerName")
    private String tellerName;
    @JsonProperty("SendBy")
    private Object sendBy;
    @JsonProperty("TellerID")
    private String tellerID;
    @JsonProperty("BankNote")
    private String bankNote;
    @JsonProperty("CbnCode")
    private Object cbnCode;
    @JsonProperty("BranchName")
    private Object branchName;
    @JsonProperty("PosId")
    private Object posId;
    @JsonProperty("TransId")
    private Object transId;
    @JsonProperty("TransRef")
    private Object transRef;
    @JsonProperty("TerminalId")
    private Object terminalId;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("Webguid")
    public String getWebguid() {
        return webguid;
    }

    @JsonProperty("Webguid")
    public void setWebguid(String webguid) {
        this.webguid = webguid;
    }

    @JsonProperty("AmountPaid")
    public String getAmountPaid() {
        return amountPaid;
    }

    @JsonProperty("AmountPaid")
    public void setAmountPaid(String amountPaid) {
        this.amountPaid = amountPaid;
    }

    @JsonProperty("PaymentRef")
    public String getPaymentRef() {
        return paymentRef;
    }

    @JsonProperty("PaymentRef")
    public void setPaymentRef(String paymentRef) {
        this.paymentRef = paymentRef;
    }

    @JsonProperty("CreditAccount")
    public String getCreditAccount() {
        return creditAccount;
    }

    @JsonProperty("CreditAccount")
    public void setCreditAccount(String creditAccount) {
        this.creditAccount = creditAccount;
    }

    @JsonProperty("Date")
    public String getDate() {
        return date;
    }

    @JsonProperty("Date")
    public void setDate(String date) {
        this.date = date;
    }

    @JsonProperty("Hash")
    public String getHash() {
        return hash;
    }

    @JsonProperty("Hash")
    public void setHash(String hash) {
        this.hash = hash;
    }

    @JsonProperty("State")
    public String getState() {
        return state;
    }

    @JsonProperty("State")
    public void setState(String state) {
        this.state = state;
    }

    @JsonProperty("ClientId")
    public String getClientId() {
        return clientId;
    }

    @JsonProperty("ClientId")
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    @JsonProperty("PaymentChannel")
    public String getPaymentChannel() {
        return paymentChannel;
    }

    @JsonProperty("PaymentChannel")
    public void setPaymentChannel(String paymentChannel) {
        this.paymentChannel = paymentChannel;
    }

    @JsonProperty("TellerName")
    public String getTellerName() {
        return tellerName;
    }

    @JsonProperty("TellerName")
    public void setTellerName(String tellerName) {
        this.tellerName = tellerName;
    }

    @JsonProperty("SendBy")
    public Object getSendBy() {
        return sendBy;
    }

    @JsonProperty("SendBy")
    public void setSendBy(Object sendBy) {
        this.sendBy = sendBy;
    }

    @JsonProperty("TellerID")
    public String getTellerID() {
        return tellerID;
    }

    @JsonProperty("TellerID")
    public void setTellerID(String tellerID) {
        this.tellerID = tellerID;
    }

    @JsonProperty("BankNote")
    public String getBankNote() {
        return bankNote;
    }

    @JsonProperty("BankNote")
    public void setBankNote(String bankNote) {
        this.bankNote = bankNote;
    }

    @JsonProperty("CbnCode")
    public Object getCbnCode() {
        return cbnCode;
    }

    @JsonProperty("CbnCode")
    public void setCbnCode(Object cbnCode) {
        this.cbnCode = cbnCode;
    }

    @JsonProperty("BranchName")
    public Object getBranchName() {
        return branchName;
    }

    @JsonProperty("BranchName")
    public void setBranchName(Object branchName) {
        this.branchName = branchName;
    }

    @JsonProperty("PosId")
    public Object getPosId() {
        return posId;
    }

    @JsonProperty("PosId")
    public void setPosId(Object posId) {
        this.posId = posId;
    }

    @JsonProperty("TransId")
    public Object getTransId() {
        return transId;
    }

    @JsonProperty("TransId")
    public void setTransId(Object transId) {
        this.transId = transId;
    }

    @JsonProperty("TransRef")
    public Object getTransRef() {
        return transRef;
    }

    @JsonProperty("TransRef")
    public void setTransRef(Object transRef) {
        this.transRef = transRef;
    }

    @JsonProperty("TerminalId")
    public Object getTerminalId() {
        return terminalId;
    }

    @JsonProperty("TerminalId")
    public void setTerminalId(Object terminalId) {
        this.terminalId = terminalId;
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
        return "PaymentNotificationDTO{" +
            "webguid='" + webguid + '\'' +
            ", amountPaid='" + amountPaid + '\'' +
            ", paymentRef='" + paymentRef + '\'' +
            ", creditAccount='" + creditAccount + '\'' +
            ", date='" + date + '\'' +
            ", hash='" + hash + '\'' +
            ", state='" + state + '\'' +
            ", clientId='" + clientId + '\'' +
            ", paymentChannel='" + paymentChannel + '\'' +
            ", tellerName='" + tellerName + '\'' +
            ", sendBy=" + sendBy +
            ", tellerID='" + tellerID + '\'' +
            ", bankNote='" + bankNote + '\'' +
            ", cbnCode=" + cbnCode +
            ", branchName=" + branchName +
            ", posId=" + posId +
            ", transId=" + transId +
            ", transRef=" + transRef +
            ", terminalId=" + terminalId +
            ", additionalProperties=" + additionalProperties +
            '}';
    }
}
