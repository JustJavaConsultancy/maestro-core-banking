package ng.com.justjava.corebanking.service.dto;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "data",
    "formatVersion",
    "clientNo"
})
@Generated("jsonschema2pojo")
public class MigoLoanRealTimeOffersRequestDTO {

    @JsonProperty("data")
    private String data;
    @JsonProperty("formatVersion")
    private String formatVersion;
    @JsonProperty("clientNo")
    private String clientNo;

    @JsonProperty("data")
    public String getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(String data) {
        this.data = data;
    }

    @JsonProperty("formatVersion")
    public String getFormatVersion() {
        return formatVersion;
    }

    @JsonProperty("formatVersion")
    public void setFormatVersion(String formatVersion) {
        this.formatVersion = formatVersion;
    }

    @JsonProperty("clientNo")
    public String getClientNo() {
        return clientNo;
    }

    @JsonProperty("clientNo")
    public void setClientNo(String clientNo) {
        this.clientNo = clientNo;
    }

    @Override
    public String toString() {
        return "MigoLoanRealTimeOffersRequestDTO{" +
            "data='" + data + '\'' +
            ", formatVersion='" + formatVersion + '\'' +
            ", clientNo='" + clientNo + '\'' +
            '}';
    }
}
