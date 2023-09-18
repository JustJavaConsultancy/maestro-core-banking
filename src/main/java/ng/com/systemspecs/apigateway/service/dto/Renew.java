package ng.com.systemspecs.apigateway.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "dueDate",
    "outstandingAmount",
    "hasDiscount",
    "invoicePeriod",
    "currentBouquet",
    "currentPlan"
})
@Generated("jsonschema2pojo")
public class Renew {

    @JsonProperty("dueDate")
    private String dueDate;
    @JsonProperty("outstandingAmount")
    private String outstandingAmount;
    @JsonProperty("hasDiscount")
    private Boolean hasDiscount;
    @JsonProperty("invoicePeriod")
    private Integer invoicePeriod;
    @JsonProperty("currentBouquet")
    private String currentBouquet;
    @JsonProperty("currentPlan")
    private CurrentPlan currentPlan;

    @JsonProperty("dueDate")
    public String getDueDate() {
        return dueDate;
    }

    @JsonProperty("dueDate")
    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    @JsonProperty("outstandingAmount")
    public String getOutstandingAmount() {
        return outstandingAmount;
    }

    @JsonProperty("outstandingAmount")
    public void setOutstandingAmount(String outstandingAmount) {
        this.outstandingAmount = outstandingAmount;
    }

    @JsonProperty("hasDiscount")
    public Boolean getHasDiscount() {
        return hasDiscount;
    }

    @JsonProperty("hasDiscount")
    public void setHasDiscount(Boolean hasDiscount) {
        this.hasDiscount = hasDiscount;
    }

    @JsonProperty("invoicePeriod")
    public Integer getInvoicePeriod() {
        return invoicePeriod;
    }

    @JsonProperty("invoicePeriod")
    public void setInvoicePeriod(Integer invoicePeriod) {
        this.invoicePeriod = invoicePeriod;
    }

    @JsonProperty("currentBouquet")
    public String getCurrentBouquet() {
        return currentBouquet;
    }

    @JsonProperty("currentBouquet")
    public void setCurrentBouquet(String currentBouquet) {
        this.currentBouquet = currentBouquet;
    }

    @JsonProperty("currentPlan")
    public CurrentPlan getCurrentPlan() {
        return currentPlan;
    }

    @JsonProperty("currentPlan")
    public void setCurrentPlan(CurrentPlan currentPlan) {
        this.currentPlan = currentPlan;
    }

    @Override
    public String toString() {
        return "Renew{" +
            "dueDate='" + dueDate + '\'' +
            ", outstandingAmount='" + outstandingAmount + '\'' +
            ", hasDiscount=" + hasDiscount +
            ", invoicePeriod=" + invoicePeriod +
            ", currentBouquet='" + currentBouquet + '\'' +
            ", currentPlan=" + currentPlan +
            '}';
    }
}
