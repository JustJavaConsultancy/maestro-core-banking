package ng.com.systemspecs.apigateway.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "status",
    "data",
    "date",
    "responseCode",
    "productCode"
})
@Generated("jsonschema2pojo")
public class LookUpDataResponseData {

    @JsonProperty("status")
    private Integer status;
    @JsonProperty("data")
    private List<LookUpDataResponseDatum> data = null;
    @JsonProperty("date")
    private String date;
    @JsonProperty("responseCode")
    private String responseCode;
    @JsonProperty("productCode")
    private String productCode;

    @JsonProperty("status")
    public Integer getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(Integer status) {
        this.status = status;
    }

    @JsonProperty("data")
    public List<LookUpDataResponseDatum> getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(List<LookUpDataResponseDatum> data) {
        this.data = data;
    }

    @JsonProperty("date")
    public String getDate() {
        return date;
    }

    @JsonProperty("date")
    public void setDate(String date) {
        this.date = date;
    }

    @JsonProperty("responseCode")
    public String getResponseCode() {
        return responseCode;
    }

    @JsonProperty("responseCode")
    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
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
        return "LookUpDataResponseData{" +
            "status=" + status +
            ", data=" + data +
            ", date='" + date + '\'' +
            ", responseCode='" + responseCode + '\'' +
            ", productCode='" + productCode + '\'' +
            '}';
    }
}
