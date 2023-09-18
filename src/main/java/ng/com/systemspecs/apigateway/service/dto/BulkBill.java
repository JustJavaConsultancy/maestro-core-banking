package ng.com.systemspecs.apigateway.service.dto;

import com.fasterxml.jackson.annotation.*;

import javax.annotation.Generated;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "TotalDue",
    "ParentGUid",
    "Pid",
    "PayerName",
    "Status",
    "StatusMessage",
    "Webguid",
    "State"
})
@Generated("jsonschema2pojo")
public class BulkBill {

    @JsonProperty("TotalDue")
    private Double totalDue;
    @JsonProperty("ParentGUid")
    private String parentGUid;
    @JsonProperty("Pid")
    private String pid;
    @JsonProperty("PayerName")
    private String payerName;
    @JsonProperty("Status")
    private String status;
    @JsonProperty("StatusMessage")
    private String statusMessage;
    @JsonProperty("Webguid")
    private List<Webguid> webguid = null;
    @JsonProperty("State")
    private String state;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("TotalDue")
    public Double getTotalDue() {
        return totalDue;
    }

    @JsonProperty("TotalDue")
    public void setTotalDue(Double totalDue) {
        this.totalDue = totalDue;
    }

    @JsonProperty("ParentGUid")
    public String getParentGUid() {
        return parentGUid;
    }

    @JsonProperty("ParentGUid")
    public void setParentGUid(String parentGUid) {
        this.parentGUid = parentGUid;
    }

    @JsonProperty("Pid")
    public String getPid() {
        return pid;
    }

    @JsonProperty("Pid")
    public void setPid(String pid) {
        this.pid = pid;
    }

    @JsonProperty("PayerName")
    public String getPayerName() {
        return payerName;
    }

    @JsonProperty("PayerName")
    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    @JsonProperty("Status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("Status")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("StatusMessage")
    public String getStatusMessage() {
        return statusMessage;
    }

    @JsonProperty("StatusMessage")
    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    @JsonProperty("Webguid")
    public List<Webguid> getWebguid() {
        return webguid;
    }

    @JsonProperty("Webguid")
    public void setWebguid(List<Webguid> webguid) {
        this.webguid = webguid;
    }

    @JsonProperty("State")
    public String getState() {
        return state;
    }

    @JsonProperty("State")
    public void setState(String state) {
        this.state = state;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public String toString() {
        return "BulkBill{" +
            "totalDue=" + totalDue +
            ", parentGUid='" + parentGUid + '\'' +
            ", pid='" + pid + '\'' +
            ", payerName='" + payerName + '\'' +
            ", status='" + status + '\'' +
            ", statusMessage='" + statusMessage + '\'' +
            ", webguid=" + webguid +
            ", state='" + state + '\'' +
            ", additionalProperties=" + additionalProperties +
            '}';
    }
}
