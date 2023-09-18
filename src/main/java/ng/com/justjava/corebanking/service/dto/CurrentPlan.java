package ng.com.justjava.corebanking.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "amount",
    "items"
})
@Generated("jsonschema2pojo")
public class CurrentPlan {

    @JsonProperty("amount")
    private Integer amount;
    @JsonProperty("items")
    private List<CurrentPlanItem> items = null;

    @JsonProperty("amount")
    public Integer getAmount() {
        return amount;
    }

    @JsonProperty("amount")
    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    @JsonProperty("items")
    public List<CurrentPlanItem> getItems() {
        return items;
    }

    @JsonProperty("items")
    public void setItems(List<CurrentPlanItem> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "CurrentPlan{" +
            "amount=" + amount +
            ", items=" + items +
            '}';
    }
}
