package ng.com.justjava.corebanking.service.dto;

import java.util.List;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "status",
    "error",
    "smartCardCode",
    "balance",
    "name",
    "bouquet",
    "message",
    "description",
    "responseCode",
    "bouquets",
    "productCode"
})
@Generated("jsonschema2pojo")
public class StartTimesValidationResponseDTO {

    @JsonProperty("status")
    private Double status;
    @JsonProperty("error")
    private Boolean error;
    @JsonProperty("smartCardCode")
    private String smartCardCode;
    @JsonProperty("balance")
    private String balance;
    @JsonProperty("name")
    private String name;
    @JsonProperty("bouquet")
    private String bouquet;
    @JsonProperty("message")
    private String message;
    @JsonProperty("description")
    private String description;
    @JsonProperty("responseCode")
    private String responseCode;
    @JsonProperty("bouquets")
    private List<StartTimesBouquet> bouquets = null;
    @JsonProperty("productCode")
    private String productCode;

    @JsonProperty("status")
    public Double getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(Double status) {
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

    @JsonProperty("smartCardCode")
    public String getSmartCardCode() {
        return smartCardCode;
    }

    @JsonProperty("smartCardCode")
    public void setSmartCardCode(String smartCardCode) {
        this.smartCardCode = smartCardCode;
    }

    @JsonProperty("balance")
    public String getBalance() {
        return balance;
    }

    @JsonProperty("balance")
    public void setBalance(String balance) {
        this.balance = balance;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("bouquet")
    public String getBouquet() {
        return bouquet;
    }

    @JsonProperty("bouquet")
    public void setBouquet(String bouquet) {
        this.bouquet = bouquet;
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

    @JsonProperty("responseCode")
    public String getResponseCode() {
        return responseCode;
    }

    @JsonProperty("responseCode")
    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    @JsonProperty("bouquets")
    public List<StartTimesBouquet> getBouquets() {
        return bouquets;
    }

    @JsonProperty("bouquets")
    public void setBouquets(List<StartTimesBouquet> bouquets) {
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
        return "StartTimesValidationResponseDTO{" +
            "status=" + status +
            ", error=" + error +
            ", smartCardCode='" + smartCardCode + '\'' +
            ", balance='" + balance + '\'' +
            ", name='" + name + '\'' +
            ", bouquet='" + bouquet + '\'' +
            ", message='" + message + '\'' +
            ", description='" + description + '\'' +
            ", responseCode='" + responseCode + '\'' +
            ", bouquets=" + bouquets +
            ", productCode='" + productCode + '\'' +
            '}';
    }
}
