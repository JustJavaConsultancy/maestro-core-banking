package ng.com.justjava.corebanking.service.dto;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "error",
    "message",
    "customerName",
    "responseCode",
    "description",
    "productCode"
})
@Generated("jsonschema2pojo")
public class InternetValidationResponseDTO {

    @JsonProperty("error")
    private Boolean error;
    @JsonProperty("message")
    private String message;
    @JsonProperty("customerName")
    private String customerName;
    @JsonProperty("responseCode")
    private String responseCode;
    @JsonProperty("description")
    private String description;
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

    @JsonProperty("customerName")
    public String getCustomerName() {
        return customerName;
    }

    @JsonProperty("customerName")
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    @JsonProperty("responseCode")
    public String getResponseCode() {
        return responseCode;
    }

    @JsonProperty("responseCode")
    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
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
        return "InternetValidationResponseDTO{" +
            "error=" + error +
            ", message='" + message + '\'' +
            ", customerName='" + customerName + '\'' +
            ", responseCode='" + responseCode + '\'' +
            ", description='" + description + '\'' +
            ", productCode='" + productCode + '\'' +
            '}';
    }
}
