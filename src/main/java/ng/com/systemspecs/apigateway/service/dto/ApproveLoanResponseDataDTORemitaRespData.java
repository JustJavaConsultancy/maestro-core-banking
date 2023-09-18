package ng.com.systemspecs.apigateway.service.dto;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "status",
    "hasData",
    "responseId",
    "responseDate",
    "requestDate",
    "responseCode",
    "responseMsg",
    "data"
})
@Generated("jsonschema2pojo")
public class ApproveLoanResponseDataDTORemitaRespData {

    @JsonProperty("status")
    private String status;
    @JsonProperty("hasData")
    private Boolean hasData;
    @JsonProperty("responseId")
    private String responseId;
    @JsonProperty("responseDate")
    private String responseDate;
    @JsonProperty("requestDate")
    private String requestDate;
    @JsonProperty("responseCode")
    private String responseCode;
    @JsonProperty("responseMsg")
    private String responseMsg;
    @JsonProperty("data")
    private ApproveLoanResponseDataDTORemitaRespDataDTO data;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("hasData")
    public Boolean getHasData() {
        return hasData;
    }

    @JsonProperty("hasData")
    public void setHasData(Boolean hasData) {
        this.hasData = hasData;
    }

    @JsonProperty("responseId")
    public String getResponseId() {
        return responseId;
    }

    @JsonProperty("responseId")
    public void setResponseId(String responseId) {
        this.responseId = responseId;
    }

    @JsonProperty("responseDate")
    public String getResponseDate() {
        return responseDate;
    }

    @JsonProperty("responseDate")
    public void setResponseDate(String responseDate) {
        this.responseDate = responseDate;
    }

    @JsonProperty("requestDate")
    public String getRequestDate() {
        return requestDate;
    }

    @JsonProperty("requestDate")
    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    @JsonProperty("responseCode")
    public String getResponseCode() {
        return responseCode;
    }

    @JsonProperty("responseCode")
    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    @JsonProperty("responseMsg")
    public String getResponseMsg() {
        return responseMsg;
    }

    @JsonProperty("responseMsg")
    public void setResponseMsg(String responseMsg) {
        this.responseMsg = responseMsg;
    }

    @JsonProperty("data")
    public ApproveLoanResponseDataDTORemitaRespDataDTO getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(ApproveLoanResponseDataDTORemitaRespDataDTO data) {
        this.data = data;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
