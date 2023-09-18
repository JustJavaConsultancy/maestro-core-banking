package ng.com.justjava.corebanking.service.dto;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "dueDate",
    "currentPlan"
})
@Generated("jsonschema2pojo")
public class ValidateMultichoiceResponseDataRenew {

    @JsonProperty("dueDate")
    private String dueDate;
    @JsonProperty("currentPlan")
    private ValidateMultichoiceResponseDataCurrentPlan currentPlan;

    @JsonProperty("dueDate")
    public String getDueDate() {
        return dueDate;
    }

    @JsonProperty("dueDate")
    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    @JsonProperty("currentPlan")
    public ValidateMultichoiceResponseDataCurrentPlan getCurrentPlan() {
        return currentPlan;
    }

    @JsonProperty("currentPlan")
    public void setCurrentPlan(ValidateMultichoiceResponseDataCurrentPlan currentPlan) {
        this.currentPlan = currentPlan;
    }

    @Override
    public String toString() {
        return "ValidateMultichoiceResponseDataRenew{" +
            "dueDate='" + dueDate + '\'' +
            ", currentPlan=" + currentPlan +
            '}';
    }
}
