package ng.com.systemspecs.apigateway.service.dto;

import java.util.List;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "amount",
    "items"
})
@Generated("jsonschema2pojo")
public class ValidateMultichoiceResponseDataCurrentPlan {

    @JsonProperty("amount")
    private Integer amount;
    @JsonProperty("items")
    private List<ValidateMultichoiceResponseDataItem> items = null;

    @JsonProperty("amount")
    public Integer getAmount() {
        return amount;
    }

    @JsonProperty("amount")
    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    @JsonProperty("items")
    public List<ValidateMultichoiceResponseDataItem> getItems() {
        return items;
    }

    @JsonProperty("items")
    public void setItems(List<ValidateMultichoiceResponseDataItem> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "ValidateMultichoiceResponseDataCurrentPlan{" +
            "amount=" + amount +
            ", items=" + items +
            '}';
    }
}
