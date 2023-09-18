package ng.com.justjava.corebanking.service.dto;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "timestamp",
    "expiration",
    "principal",
    "fee",
    "term"
})
@Generated("jsonschema2pojo")
public class MigoGetLoanResponseDTO {

    @JsonProperty("id")
    private String id;
    @JsonProperty("timestamp")
    private String timestamp;
    @JsonProperty("expiration")
    private String expiration;
    @JsonProperty("principal")
    private Integer principal;
    @JsonProperty("fee")
    private Integer fee;
    @JsonProperty("term")
    private Integer term;

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("timestamp")
    public String getTimestamp() {
        return timestamp;
    }

    @JsonProperty("timestamp")
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @JsonProperty("expiration")
    public String getExpiration() {
        return expiration;
    }

    @JsonProperty("expiration")
    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    @JsonProperty("principal")
    public Integer getPrincipal() {
        return principal;
    }

    @JsonProperty("principal")
    public void setPrincipal(Integer principal) {
        this.principal = principal;
    }

    @JsonProperty("fee")
    public Integer getFee() {
        return fee;
    }

    @JsonProperty("fee")
    public void setFee(Integer fee) {
        this.fee = fee;
    }

    @JsonProperty("term")
    public Integer getTerm() {
        return term;
    }

    @JsonProperty("term")
    public void setTerm(Integer term) {
        this.term = term;
    }

    @Override
    public String toString() {
        return "MigoGetLoanResponseDTO{" +
            "id='" + id + '\'' +
            ", timestamp='" + timestamp + '\'' +
            ", expiration='" + expiration + '\'' +
            ", principal=" + principal +
            ", fee=" + fee +
            ", term=" + term +
            '}';
    }
}
