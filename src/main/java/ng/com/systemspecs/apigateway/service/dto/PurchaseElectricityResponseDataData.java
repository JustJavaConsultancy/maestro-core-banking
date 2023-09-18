package ng.com.systemspecs.apigateway.service.dto;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "status",
    "error",
    "message",
    "date",
    "ref",
    "token",
    "address",
    "payer",
    "amount",
    "account_type",
    "kct",
    "client_id",
    "sgc",
    "msno",
    "unit_cost",
    "tran_id",
    "krn",
    "ti",
    "tt",
    "unit",
    "adjust_unit",
    "preset_unit",
    "total_unit",
    "unit_value",
    "manuf",
    "model",
    "feederBand",
    "feederName",
    "arrears",
    "balance",
    "refund",
    "walletBalance",
    "tariff",
    "tariff_class",
    "vatRate",
    "response_code",
    "transactionUniqueNumber",
    "transactionDateTime",
    "transactionDebitTransactionId",
    "dealer_name",
    "agent_name",
    "agent_code",
    "rate",
    "type",
    "tokenList",
    "outstandingDebt",
    "costOfUnit",
    "vat",
    "fixedCharge",
    "reconnectionFee",
    "lor",
    "administrativeCharge",
    "installationFee",
    "penalty",
    "meterCost",
    "currentCharge",
    "msc",
    "responseCode",
    "reference",
    "sequence",
    "clientReference"
})
@Generated("jsonschema2pojo")
public class PurchaseElectricityResponseDataData {

    @JsonProperty("status")
    private Integer status;
    @JsonProperty("error")
    private Boolean error;
    @JsonProperty("message")
    private String message;
    @JsonProperty("date")
    private String date;
    @JsonProperty("ref")
    private String ref;
    @JsonProperty("token")
    private String token;
    @JsonProperty("address")
    private String address;
    @JsonProperty("payer")
    private String payer;
    @JsonProperty("amount")
    private Double amount;
    @JsonProperty("account_type")
    private String account_type;
    @JsonProperty("kct")
    private String kct;
    @JsonProperty("client_id")
    private String client_id;
    @JsonProperty("sgc")
    private String sgc;
    @JsonProperty("msno")
    private String msno;
    @JsonProperty("unit_cost")
    private String unit_cost;
    @JsonProperty("tran_id")
    private String tran_id;
    @JsonProperty("krn")
    private String krn;
    @JsonProperty("ti")
    private String ti;
    @JsonProperty("tt")
    private String tt;
    @JsonProperty("unit")
    private String unit;
    @JsonProperty("adjust_unit")
    private String adjust_unit;
    @JsonProperty("preset_unit")
    private String preset_unit;
    @JsonProperty("total_unit")
    private String total_unit;
    @JsonProperty("unit_value")
    private String unit_value;
    @JsonProperty("manuf")
    private String manuf;
    @JsonProperty("model")
    private String model;
    @JsonProperty("feederBand")
    private String feederBand;
    @JsonProperty("feederName")
    private String feederName;
    @JsonProperty("arrears")
    private String arrears;
    @JsonProperty("balance")
    private String balance;
    @JsonProperty("refund")
    private String refund;
    @JsonProperty("walletBalance")
    private String walletBalance;
    @JsonProperty("tariff")
    private String tariff;
    @JsonProperty("tariff_class")
    private String tariff_class;
    @JsonProperty("vatRate")
    private String vatRate;
    @JsonProperty("response_code")
    private String response_code;
    @JsonProperty("transactionUniqueNumber")
    private String transactionUniqueNumber;
    @JsonProperty("transactionDateTime")
    private String transactionDateTime;
    @JsonProperty("transactionDebitTransactionId")
    private String transactionDebitTransactionId;
    @JsonProperty("dealer_name")
    private String dealer_name;
    @JsonProperty("agent_name")
    private String agent_name;
    @JsonProperty("agent_code")
    private String agent_code;
    @JsonProperty("rate")
    private String rate;
    @JsonProperty("type")
    private String type;
    @JsonProperty("tokenList")
    private String tokenList;
    @JsonProperty("outstandingDebt")
    private String outstandingDebt;
    @JsonProperty("costOfUnit")
    private String costOfUnit;
    @JsonProperty("vat")
    private String vat;
    @JsonProperty("fixedCharge")
    private String fixedCharge;
    @JsonProperty("reconnectionFee")
    private String reconnectionFee;
    @JsonProperty("lor")
    private String lor;
    @JsonProperty("administrativeCharge")
    private String administrativeCharge;
    @JsonProperty("installationFee")
    private String installationFee;
    @JsonProperty("penalty")
    private String penalty;
    @JsonProperty("meterCost")
    private String meterCost;
    @JsonProperty("currentCharge")
    private String currentCharge;
    @JsonProperty("msc")
    private String msc;
    @JsonProperty("responseCode")
    private String responseCode;
    @JsonProperty("reference")
    private String reference;
    @JsonProperty("sequence")
    private String sequence;
    @JsonProperty("clientReference")
    private String clientReference;

    @JsonProperty("status")
    public Integer getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(Integer status) {
        this.status = status;
    }

    @JsonProperty("error")
    public Boolean getError() {
        return error;
    }

    @JsonProperty("error")
    public void setError(Boolean error) {
        this.error = error;
    }

    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    @JsonProperty("message")
    public void setMessage(String message) {
        this.message = message;
    }

    @JsonProperty("date")
    public String getDate() {
        return date;
    }

    @JsonProperty("date")
    public void setDate(String date) {
        this.date = date;
    }

    @JsonProperty("ref")
    public String getRef() {
        return ref;
    }

    @JsonProperty("ref")
    public void setRef(String ref) {
        this.ref = ref;
    }

    @JsonProperty("token")
    public String getToken() {
        return token;
    }

    @JsonProperty("token")
    public void setToken(String token) {
        this.token = token;
    }

    @JsonProperty("address")
    public String getAddress() {
        return address;
    }

    @JsonProperty("address")
    public void setAddress(String address) {
        this.address = address;
    }

    @JsonProperty("payer")
    public String getPayer() {
        return payer;
    }

    @JsonProperty("payer")
    public void setPayer(String payer) {
        this.payer = payer;
    }

    @JsonProperty("amount")
    public Double getAmount() {
        return amount;
    }

    @JsonProperty("amount")
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @JsonProperty("account_type")
    public String getAccount_type() {
        return account_type;
    }

    @JsonProperty("account_type")
    public void setAccount_type(String account_type) {
        this.account_type = account_type;
    }

    @JsonProperty("kct")
    public String getKct() {
        return kct;
    }

    @JsonProperty("kct")
    public void setKct(String kct) {
        this.kct = kct;
    }

    @JsonProperty("client_id")
    public String getClient_id() {
        return client_id;
    }

    @JsonProperty("client_id")
    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    @JsonProperty("sgc")
    public String getSgc() {
        return sgc;
    }

    @JsonProperty("sgc")
    public void setSgc(String sgc) {
        this.sgc = sgc;
    }

    @JsonProperty("msno")
    public String getMsno() {
        return msno;
    }

    @JsonProperty("msno")
    public void setMsno(String msno) {
        this.msno = msno;
    }

    @JsonProperty("unit_cost")
    public String getUnit_cost() {
        return unit_cost;
    }

    @JsonProperty("unit_cost")
    public void setUnit_cost(String unit_cost) {
        this.unit_cost = unit_cost;
    }

    @JsonProperty("tran_id")
    public String getTran_id() {
        return tran_id;
    }

    @JsonProperty("tran_id")
    public void setTran_id(String tran_id) {
        this.tran_id = tran_id;
    }

    @JsonProperty("krn")
    public String getKrn() {
        return krn;
    }

    @JsonProperty("krn")
    public void setKrn(String krn) {
        this.krn = krn;
    }

    @JsonProperty("ti")
    public String getTi() {
        return ti;
    }

    @JsonProperty("ti")
    public void setTi(String ti) {
        this.ti = ti;
    }

    @JsonProperty("tt")
    public String getTt() {
        return tt;
    }

    @JsonProperty("tt")
    public void setTt(String tt) {
        this.tt = tt;
    }

    @JsonProperty("unit")
    public String getUnit() {
        return unit;
    }

    @JsonProperty("unit")
    public void setUnit(String unit) {
        this.unit = unit;
    }

    @JsonProperty("adjust_unit")
    public String getAdjust_unit() {
        return adjust_unit;
    }

    @JsonProperty("adjust_unit")
    public void setAdjust_unit(String adjust_unit) {
        this.adjust_unit = adjust_unit;
    }

    @JsonProperty("preset_unit")
    public String getPreset_unit() {
        return preset_unit;
    }

    @JsonProperty("preset_unit")
    public void setPreset_unit(String preset_unit) {
        this.preset_unit = preset_unit;
    }

    @JsonProperty("total_unit")
    public String getTotal_unit() {
        return total_unit;
    }

    @JsonProperty("total_unit")
    public void setTotal_unit(String total_unit) {
        this.total_unit = total_unit;
    }

    @JsonProperty("unit_value")
    public String getUnit_value() {
        return unit_value;
    }

    @JsonProperty("unit_value")
    public void setUnit_value(String unit_value) {
        this.unit_value = unit_value;
    }

    @JsonProperty("manuf")
    public String getManuf() {
        return manuf;
    }

    @JsonProperty("manuf")
    public void setManuf(String manuf) {
        this.manuf = manuf;
    }

    @JsonProperty("model")
    public String getModel() {
        return model;
    }

    @JsonProperty("model")
    public void setModel(String model) {
        this.model = model;
    }

    @JsonProperty("feederBand")
    public String getFeederBand() {
        return feederBand;
    }

    @JsonProperty("feederBand")
    public void setFeederBand(String feederBand) {
        this.feederBand = feederBand;
    }

    @JsonProperty("feederName")
    public String getFeederName() {
        return feederName;
    }

    @JsonProperty("feederName")
    public void setFeederName(String feederName) {
        this.feederName = feederName;
    }

    @JsonProperty("arrears")
    public String getArrears() {
        return arrears;
    }

    @JsonProperty("arrears")
    public void setArrears(String arrears) {
        this.arrears = arrears;
    }

    @JsonProperty("balance")
    public String getBalance() {
        return balance;
    }

    @JsonProperty("balance")
    public void setBalance(String balance) {
        this.balance = balance;
    }

    @JsonProperty("refund")
    public String getRefund() {
        return refund;
    }

    @JsonProperty("refund")
    public void setRefund(String refund) {
        this.refund = refund;
    }

    @JsonProperty("walletBalance")
    public String getWalletBalance() {
        return walletBalance;
    }

    @JsonProperty("walletBalance")
    public void setWalletBalance(String walletBalance) {
        this.walletBalance = walletBalance;
    }

    @JsonProperty("tariff")
    public String getTariff() {
        return tariff;
    }

    @JsonProperty("tariff")
    public void setTariff(String tariff) {
        this.tariff = tariff;
    }

    @JsonProperty("tariff_class")
    public String getTariff_class() {
        return tariff_class;
    }

    @JsonProperty("tariff_class")
    public void setTariff_class(String tariff_class) {
        this.tariff_class = tariff_class;
    }

    @JsonProperty("vatRate")
    public String getVatRate() {
        return vatRate;
    }

    @JsonProperty("vatRate")
    public void setVatRate(String vatRate) {
        this.vatRate = vatRate;
    }

    @JsonProperty("response_code")
    public String getResponse_code() {
        return response_code;
    }

    @JsonProperty("response_code")
    public void setResponse_code(String response_code) {
        this.response_code = response_code;
    }

    @JsonProperty("transactionUniqueNumber")
    public String getTransactionUniqueNumber() {
        return transactionUniqueNumber;
    }

    @JsonProperty("transactionUniqueNumber")
    public void setTransactionUniqueNumber(String transactionUniqueNumber) {
        this.transactionUniqueNumber = transactionUniqueNumber;
    }

    @JsonProperty("transactionDateTime")
    public String getTransactionDateTime() {
        return transactionDateTime;
    }

    @JsonProperty("transactionDateTime")
    public void setTransactionDateTime(String transactionDateTime) {
        this.transactionDateTime = transactionDateTime;
    }

    @JsonProperty("transactionDebitTransactionId")
    public String getTransactionDebitTransactionId() {
        return transactionDebitTransactionId;
    }

    @JsonProperty("transactionDebitTransactionId")
    public void setTransactionDebitTransactionId(String transactionDebitTransactionId) {
        this.transactionDebitTransactionId = transactionDebitTransactionId;
    }

    @JsonProperty("dealer_name")
    public String getDealer_name() {
        return dealer_name;
    }

    @JsonProperty("dealer_name")
    public void setDealer_name(String dealer_name) {
        this.dealer_name = dealer_name;
    }

    @JsonProperty("agent_name")
    public String getAgent_name() {
        return agent_name;
    }

    @JsonProperty("agent_name")
    public void setAgent_name(String agent_name) {
        this.agent_name = agent_name;
    }

    @JsonProperty("agent_code")
    public String getAgent_code() {
        return agent_code;
    }

    @JsonProperty("agent_code")
    public void setAgent_code(String agent_code) {
        this.agent_code = agent_code;
    }

    @JsonProperty("rate")
    public String getRate() {
        return rate;
    }

    @JsonProperty("rate")
    public void setRate(String rate) {
        this.rate = rate;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("tokenList")
    public String getTokenList() {
        return tokenList;
    }

    @JsonProperty("tokenList")
    public void setTokenList(String tokenList) {
        this.tokenList = tokenList;
    }

    @JsonProperty("outstandingDebt")
    public String getOutstandingDebt() {
        return outstandingDebt;
    }

    @JsonProperty("outstandingDebt")
    public void setOutstandingDebt(String outstandingDebt) {
        this.outstandingDebt = outstandingDebt;
    }

    @JsonProperty("costOfUnit")
    public String getCostOfUnit() {
        return costOfUnit;
    }

    @JsonProperty("costOfUnit")
    public void setCostOfUnit(String costOfUnit) {
        this.costOfUnit = costOfUnit;
    }

    @JsonProperty("vat")
    public String getVat() {
        return vat;
    }

    @JsonProperty("vat")
    public void setVat(String vat) {
        this.vat = vat;
    }

    @JsonProperty("fixedCharge")
    public String getFixedCharge() {
        return fixedCharge;
    }

    @JsonProperty("fixedCharge")
    public void setFixedCharge(String fixedCharge) {
        this.fixedCharge = fixedCharge;
    }

    @JsonProperty("reconnectionFee")
    public String getReconnectionFee() {
        return reconnectionFee;
    }

    @JsonProperty("reconnectionFee")
    public void setReconnectionFee(String reconnectionFee) {
        this.reconnectionFee = reconnectionFee;
    }

    @JsonProperty("lor")
    public String getLor() {
        return lor;
    }

    @JsonProperty("lor")
    public void setLor(String lor) {
        this.lor = lor;
    }

    @JsonProperty("administrativeCharge")
    public String getAdministrativeCharge() {
        return administrativeCharge;
    }

    @JsonProperty("administrativeCharge")
    public void setAdministrativeCharge(String administrativeCharge) {
        this.administrativeCharge = administrativeCharge;
    }

    @JsonProperty("installationFee")
    public String getInstallationFee() {
        return installationFee;
    }

    @JsonProperty("installationFee")
    public void setInstallationFee(String installationFee) {
        this.installationFee = installationFee;
    }

    @JsonProperty("penalty")
    public String getPenalty() {
        return penalty;
    }

    @JsonProperty("penalty")
    public void setPenalty(String penalty) {
        this.penalty = penalty;
    }

    @JsonProperty("meterCost")
    public String getMeterCost() {
        return meterCost;
    }

    @JsonProperty("meterCost")
    public void setMeterCost(String meterCost) {
        this.meterCost = meterCost;
    }

    @JsonProperty("currentCharge")
    public String getCurrentCharge() {
        return currentCharge;
    }

    @JsonProperty("currentCharge")
    public void setCurrentCharge(String currentCharge) {
        this.currentCharge = currentCharge;
    }

    @JsonProperty("msc")
    public String getMsc() {
        return msc;
    }

    @JsonProperty("msc")
    public void setMsc(String msc) {
        this.msc = msc;
    }

    @JsonProperty("responseCode")
    public String getResponseCode() {
        return responseCode;
    }

    @JsonProperty("responseCode")
    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    @JsonProperty("reference")
    public String getReference() {
        return reference;
    }

    @JsonProperty("reference")
    public void setReference(String reference) {
        this.reference = reference;
    }

    @JsonProperty("sequence")
    public String getSequence() {
        return sequence;
    }

    @JsonProperty("sequence")
    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    @JsonProperty("clientReference")
    public String getClientReference() {
        return clientReference;
    }

    @JsonProperty("clientReference")
    public void setClientReference(String clientReference) {
        this.clientReference = clientReference;
    }

    @Override
    public String toString() {
        return "PurchaseElectricityResponseDataData{" +
            "status=" + status +
            ", error=" + error +
            ", message='" + message + '\'' +
            ", date='" + date + '\'' +
            ", ref='" + ref + '\'' +
            ", token='" + token + '\'' +
            ", address='" + address + '\'' +
            ", payer='" + payer + '\'' +
            ", amount=" + amount +
            ", account_type='" + account_type + '\'' +
            ", kct='" + kct + '\'' +
            ", client_id='" + client_id + '\'' +
            ", sgc='" + sgc + '\'' +
            ", msno='" + msno + '\'' +
            ", unit_cost='" + unit_cost + '\'' +
            ", tran_id='" + tran_id + '\'' +
            ", krn='" + krn + '\'' +
            ", ti='" + ti + '\'' +
            ", tt='" + tt + '\'' +
            ", unit='" + unit + '\'' +
            ", adjust_unit='" + adjust_unit + '\'' +
            ", preset_unit='" + preset_unit + '\'' +
            ", total_unit='" + total_unit + '\'' +
            ", unit_value='" + unit_value + '\'' +
            ", manuf='" + manuf + '\'' +
            ", model='" + model + '\'' +
            ", feederBand='" + feederBand + '\'' +
            ", feederName='" + feederName + '\'' +
            ", arrears='" + arrears + '\'' +
            ", balance='" + balance + '\'' +
            ", refund='" + refund + '\'' +
            ", walletBalance='" + walletBalance + '\'' +
            ", tariff='" + tariff + '\'' +
            ", tariff_class='" + tariff_class + '\'' +
            ", vatRate='" + vatRate + '\'' +
            ", response_code='" + response_code + '\'' +
            ", transactionUniqueNumber='" + transactionUniqueNumber + '\'' +
            ", transactionDateTime='" + transactionDateTime + '\'' +
            ", transactionDebitTransactionId='" + transactionDebitTransactionId + '\'' +
            ", dealer_name='" + dealer_name + '\'' +
            ", agent_name='" + agent_name + '\'' +
            ", agent_code='" + agent_code + '\'' +
            ", rate='" + rate + '\'' +
            ", type='" + type + '\'' +
            ", tokenList='" + tokenList + '\'' +
            ", outstandingDebt='" + outstandingDebt + '\'' +
            ", costOfUnit='" + costOfUnit + '\'' +
            ", vat='" + vat + '\'' +
            ", fixedCharge='" + fixedCharge + '\'' +
            ", reconnectionFee='" + reconnectionFee + '\'' +
            ", lor='" + lor + '\'' +
            ", administrativeCharge='" + administrativeCharge + '\'' +
            ", installationFee='" + installationFee + '\'' +
            ", penalty='" + penalty + '\'' +
            ", meterCost='" + meterCost + '\'' +
            ", currentCharge='" + currentCharge + '\'' +
            ", msc='" + msc + '\'' +
            ", responseCode='" + responseCode + '\'' +
            ", reference='" + reference + '\'' +
            ", sequence='" + sequence + '\'' +
            ", clientReference='" + clientReference + '\'' +
            '}';
    }
}
