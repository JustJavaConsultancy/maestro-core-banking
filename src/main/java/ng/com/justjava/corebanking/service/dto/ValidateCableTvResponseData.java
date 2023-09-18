package ng.com.justjava.corebanking.service.dto;

//public class ValidateCableTvResponseData


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "error",
    "ref",
    "name",
    "unit",
    "date",
    "renew",
    "bouquetCode",
    "serviceId",
    "outstandingAmount",
    "customerNumber",
    "responseCode",
    "message",
    "description",
    "account",
    "type",
    "basketId",
    "bouquets",
    "productCode"
})
@Generated("jsonschema2pojo")
public class ValidateCableTvResponseData {

    @JsonProperty("error")
    private Boolean error;
    @JsonProperty("ref")
    private String ref;
    @JsonProperty("name")
    private String name;
    @JsonProperty("unit")
    private String unit;
    @JsonProperty("date")
    private String date;
    @JsonProperty("renew")
    private Renew renew;
    @JsonProperty("bouquetCode")
    private String bouquetCode;
    @JsonProperty("serviceId")
    private String serviceId;
    @JsonProperty("outstandingAmount")
    private Integer outstandingAmount;
    @JsonProperty("customerNumber")
    private String customerNumber;
    @JsonProperty("responseCode")
    private String responseCode;
    @JsonProperty("message")
    private String message;
    @JsonProperty("description")
    private String description;
    @JsonProperty("account")
    private String account;
    @JsonProperty("type")
    private String type;
    @JsonProperty("basketId")
    private String basketId;
    @JsonProperty("bouquets")
    private List<Bouquet> bouquets = null;
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

    @JsonProperty("ref")
    public String getRef() {
        return ref;
    }

    @JsonProperty("ref")
    public void setRef(String ref) {
        this.ref = ref;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("unit")
    public String getUnit() {
        return unit;
    }

    @JsonProperty("unit")
    public void setUnit(String unit) {
        this.unit = unit;
    }

    @JsonProperty("date")
    public String getDate() {
        return date;
    }

    @JsonProperty("date")
    public void setDate(String date) {
        this.date = date;
    }

    @JsonProperty("bouquetCode")
    public String getBouquetCode() {
        return bouquetCode;
    }

    @JsonProperty("bouquetCode")
    public void setBouquetCode(String bouquetCode) {
        this.bouquetCode = bouquetCode;
    }

    @JsonProperty("serviceId")
    public String getServiceId() {
        return serviceId;
    }

    @JsonProperty("serviceId")
    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    @JsonProperty("customerNumber")
    public String getCustomerNumber() {
        return customerNumber;
    }

    @JsonProperty("customerNumber")
    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    @JsonProperty("responseCode")
    public String getResponseCode() {
        return responseCode;
    }

    @JsonProperty("responseCode")
    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    @JsonProperty("message")
    public void setMessage(String message) {
        this.message = message;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("account")
    public String getAccount() {
        return account;
    }

    @JsonProperty("account")
    public void setAccount(String account) {
        this.account = account;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("basketId")
    public String getBasketId() {
        return basketId;
    }

    @JsonProperty("basketId")
    public void setBasketId(String basketId) {
        this.basketId = basketId;
    }

    @JsonProperty("bouquets")
    public List<Bouquet> getBouquets() {
        return bouquets;
    }

    @JsonProperty("bouquets")
    public void setBouquets(List<Bouquet> bouquets) {
        this.bouquets = bouquets;
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
        return "ValidateCableTvDTO{" +
            "error=" + error +
            ", ref='" + ref + '\'' +
            ", name='" + name + '\'' +
            ", unit='" + unit + '\'' +
            ", date='" + date + '\'' +
            ", bouquetCode='" + bouquetCode + '\'' +
            ", serviceId='" + serviceId + '\'' +
            ", customerNumber='" + customerNumber + '\'' +
            ", responseCode='" + responseCode + '\'' +
            ", message='" + message + '\'' +
            ", description='" + description + '\'' +
            ", account='" + account + '\'' +
            ", type='" + type + '\'' +
            ", basketId='" + basketId + '\'' +
            ", bouquets=" + bouquets +
            ", productCode='" + productCode + '\'' +
            '}';
    }
}
