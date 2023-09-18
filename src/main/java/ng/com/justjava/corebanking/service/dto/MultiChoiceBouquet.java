package ng.com.justjava.corebanking.service.dto;

import java.util.List;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "code",
    "name",
    "amount",
    "primaryAmount",
    "availablePricingOptions"
})
@Generated("jsonschema2pojo")
public class MultiChoiceBouquet {

    @JsonProperty("code")
    private String code;
    @JsonProperty("name")
    private String name;
    @JsonProperty("amount")
    private String amount;
    @JsonProperty("primaryAmount")
    private String primaryAmount;
    @JsonProperty("availablePricingOptions")
    private List<AvailablePricingOption> availablePricingOptions = null;

    @JsonProperty("code")
    public String getCode() {
        return code;
    }

    @JsonProperty("code")
    public void setCode(String code) {
        this.code = code;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("amount")
    public String getAmount() {
        return amount;
    }

    @JsonProperty("amount")
    public void setAmount(String amount) {
        this.amount = amount;
    }

    @JsonProperty("primaryAmount")
    public String getPrimaryAmount() {
        return primaryAmount;
    }

    @JsonProperty("primaryAmount")
    public void setPrimaryAmount(String primaryAmount) {
        this.primaryAmount = primaryAmount;
    }

    @JsonProperty("availablePricingOptions")
    public List<AvailablePricingOption> getAvailablePricingOptions() {
        return availablePricingOptions;
    }

    @JsonProperty("availablePricingOptions")
    public void setAvailablePricingOptions(List<AvailablePricingOption> availablePricingOptions) {
        this.availablePricingOptions = availablePricingOptions;
    }

    @Override
    public String toString() {
        return "Bouquet{" +
            "code='" + code + '\'' +
            ", name='" + name + '\'' +
            ", amount='" + amount + '\'' +
            ", primaryAmount='" + primaryAmount + '\'' +
            ", availablePricingOptions=" + availablePricingOptions +
            '}';
    }
}
