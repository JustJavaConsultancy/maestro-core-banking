package ng.com.systemspecs.apigateway.service.dto;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "error",
    "message",
    "customerId",
    "name",
    "meterNumber",
    "accountNumber",
    "businessUnit",
    "businessUnitId",
    "undertaking",
    "phone",
    "address",
    "email",
    "lastTransactionDate",
    "minimumPurchase",
    "customerArrears",
    "tariffCode",
    "tariff",
    "description",
    "customerType",
    "responseCode",
    "productCode"
})
@Generated("jsonschema2pojo")
public class MeterValidationResponseDTOData {

    @JsonProperty("error")
    private Boolean error;
    @JsonProperty("message")
    private String message;
    @JsonProperty("customerId")
    private String customerId;
    @JsonProperty("name")
    private String name;
    @JsonProperty("meterNumber")
    private String meterNumber;
    @JsonProperty("accountNumber")
    private String accountNumber;
    @JsonProperty("businessUnit")
    private String businessUnit;
    @JsonProperty("businessUnitId")
    private String businessUnitId;
    @JsonProperty("undertaking")
    private String undertaking;
    @JsonProperty("phone")
    private String phone;
    @JsonProperty("address")
    private String address;
    @JsonProperty("email")
    private String email;
    @JsonProperty("lastTransactionDate")
    private String lastTransactionDate;
    @JsonProperty("minimumPurchase")
    private String minimumPurchase;
    @JsonProperty("customerArrears")
    private String customerArrears;
    @JsonProperty("tariffCode")
    private String tariffCode;
    @JsonProperty("tariff")
    private String tariff;
    @JsonProperty("description")
    private String description;
    @JsonProperty("customerType")
    private String customerType;
    @JsonProperty("responseCode")
    private String responseCode;
    @JsonProperty("productCode")
    private String productCode;

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

    @JsonProperty("customerId")
    public String getCustomerId() {
        return customerId;
    }

    @JsonProperty("customerId")
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("meterNumber")
    public String getMeterNumber() {
        return meterNumber;
    }

    @JsonProperty("meterNumber")
    public void setMeterNumber(String meterNumber) {
        this.meterNumber = meterNumber;
    }

    @JsonProperty("accountNumber")
    public String getAccountNumber() {
        return accountNumber;
    }

    @JsonProperty("accountNumber")
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    @JsonProperty("businessUnit")
    public String getBusinessUnit() {
        return businessUnit;
    }

    @JsonProperty("businessUnit")
    public void setBusinessUnit(String businessUnit) {
        this.businessUnit = businessUnit;
    }

    @JsonProperty("businessUnitId")
    public String getBusinessUnitId() {
        return businessUnitId;
    }

    @JsonProperty("businessUnitId")
    public void setBusinessUnitId(String businessUnitId) {
        this.businessUnitId = businessUnitId;
    }

    @JsonProperty("undertaking")
    public String getUndertaking() {
        return undertaking;
    }

    @JsonProperty("undertaking")
    public void setUndertaking(String undertaking) {
        this.undertaking = undertaking;
    }

    @JsonProperty("phone")
    public String getPhone() {
        return phone;
    }

    @JsonProperty("phone")
    public void setPhone(String phone) {
        this.phone = phone;
    }

    @JsonProperty("address")
    public String getAddress() {
        return address;
    }

    @JsonProperty("address")
    public void setAddress(String address) {
        this.address = address;
    }

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    @JsonProperty("email")
    public void setEmail(String email) {
        this.email = email;
    }

    @JsonProperty("lastTransactionDate")
    public String getLastTransactionDate() {
        return lastTransactionDate;
    }

    @JsonProperty("lastTransactionDate")
    public void setLastTransactionDate(String lastTransactionDate) {
        this.lastTransactionDate = lastTransactionDate;
    }

    @JsonProperty("minimumPurchase")
    public String getMinimumPurchase() {
        return minimumPurchase;
    }

    @JsonProperty("minimumPurchase")
    public void setMinimumPurchase(String minimumPurchase) {
        this.minimumPurchase = minimumPurchase;
    }

    @JsonProperty("customerArrears")
    public String getCustomerArrears() {
        return customerArrears;
    }

    @JsonProperty("customerArrears")
    public void setCustomerArrears(String customerArrears) {
        this.customerArrears = customerArrears;
    }

    @JsonProperty("tariffCode")
    public String getTariffCode() {
        return tariffCode;
    }

    @JsonProperty("tariffCode")
    public void setTariffCode(String tariffCode) {
        this.tariffCode = tariffCode;
    }

    @JsonProperty("tariff")
    public String getTariff() {
        return tariff;
    }

    @JsonProperty("tariff")
    public void setTariff(String tariff) {
        this.tariff = tariff;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("customerType")
    public String getCustomerType() {
        return customerType;
    }

    @JsonProperty("customerType")
    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    @JsonProperty("responseCode")
    public String getResponseCode() {
        return responseCode;
    }

    @JsonProperty("responseCode")
    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    @JsonProperty("productCode")
    public String getProductCode() {
        return productCode;
    }

    @JsonProperty("productCode")
    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    @Override
    public String toString() {
        return "Data{" +
            "error=" + error +
            ", message='" + message + '\'' +
            ", customerId='" + customerId + '\'' +
            ", name='" + name + '\'' +
            ", meterNumber='" + meterNumber + '\'' +
            ", accountNumber='" + accountNumber + '\'' +
            ", businessUnit='" + businessUnit + '\'' +
            ", businessUnitId='" + businessUnitId + '\'' +
            ", undertaking='" + undertaking + '\'' +
            ", phone='" + phone + '\'' +
            ", address='" + address + '\'' +
            ", email='" + email + '\'' +
            ", lastTransactionDate='" + lastTransactionDate + '\'' +
            ", minimumPurchase='" + minimumPurchase + '\'' +
            ", customerArrears='" + customerArrears + '\'' +
            ", tariffCode='" + tariffCode + '\'' +
            ", tariff='" + tariff + '\'' +
            ", description='" + description + '\'' +
            ", customerType='" + customerType + '\'' +
            ", responseCode='" + responseCode + '\'' +
            ", productCode='" + productCode + '\'' +
            '}';
    }
}
