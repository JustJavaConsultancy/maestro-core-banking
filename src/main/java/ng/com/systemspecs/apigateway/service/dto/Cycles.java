package ng.com.systemspecs.apigateway.service.dto;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "daily",
    "weekly",
    "monthly"
})
@Generated("jsonschema2pojo")
public class Cycles {

    @JsonProperty("daily")
    private Double daily;
    @JsonProperty("weekly")
    private Double weekly;
    @JsonProperty("monthly")
    private Double monthly;

    @JsonProperty("daily")
    public Double getDaily() {
        return daily;
    }

    @JsonProperty("daily")
    public void setDaily(Double daily) {
        this.daily = daily;
    }

    @JsonProperty("weekly")
    public Double getWeekly() {
        return weekly;
    }

    @JsonProperty("weekly")
    public void setWeekly(Double weekly) {
        this.weekly = weekly;
    }

    @JsonProperty("monthly")
    public Double getMonthly() {
        return monthly;
    }

    @JsonProperty("monthly")
    public void setMonthly(Double monthly) {
        this.monthly = monthly;
    }

    @Override
    public String toString() {
        return "Cycles{" +
            "daily=" + daily +
            ", weekly=" + weekly +
            ", monthly=" + monthly +
            '}';
    }
}
