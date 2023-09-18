package ng.com.justjava.corebanking.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "name",
    "product_code",
    "amount",
    "code",
    "description"
})
@Generated("jsonschema2pojo")
public class Bouquet {

    @JsonProperty("name")
    private String name;
    @JsonProperty("product_code")
    private String productCode;
    @JsonProperty("amount")
    private String amount;
    @JsonProperty("code")
    private String code;
    @JsonProperty("description")
    private String description;
    @JsonProperty("primaryAmount")
    private String primaryAmount;


    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("product_code")
    public String getProductCode() {
        return productCode;
    }

    @JsonProperty("product_code")
    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    @JsonProperty("amount")
    public String getAmount() {
        return amount;
    }

    @JsonProperty("amount")
    public void setAmount(String amount) {
        this.amount = amount;
    }

    @JsonProperty("code")
    public String getCode() {
        return code;
    }

    @JsonProperty("code")
    public void setCode(String code) {
        this.code = code;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    public String toString() {
        return "Bouquet{" +
            "name='" + name + '\'' +
            ", productCode='" + productCode + '\'' +
            ", amount='" + amount + '\'' +
            ", code='" + code + '\'' +
            ", description='" + description + '\'' +
            '}';
    }
}
