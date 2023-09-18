package ng.com.justjava.corebanking.service.dto;

import java.util.List;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "responseCode",
    "error",
    "bouquets"
})
@Generated("jsonschema2pojo")
public class MultiChoiceBouquetsResponseDTO {

    @JsonProperty("responseCode")
    private String responseCode;
    @JsonProperty("error")
    private Boolean error;
    @JsonProperty("bouquets")
    private List<MultiChoiceBouquet> bouquets = null;

    @JsonProperty("responseCode")
    public String getResponseCode() {
        return responseCode;
    }

    @JsonProperty("responseCode")
    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    @JsonProperty("error")
    public Boolean getError() {
        return error;
    }

    @JsonProperty("error")
    public void setError(Boolean error) {
        this.error = error;
    }

    @JsonProperty("bouquets")
    public List<MultiChoiceBouquet> getBouquets() {
        return bouquets;
    }

    @JsonProperty("bouquets")
    public void setBouquets(List<MultiChoiceBouquet> bouquets) {
        this.bouquets = bouquets;
    }

    @Override
    public String toString() {
        return "MultiChoiceBouquetsResponseDTO{" +
            "responseCode='" + responseCode + '\'' +
            ", error=" + error +
            ", bouquets=" + bouquets +
            '}';
    }
}
