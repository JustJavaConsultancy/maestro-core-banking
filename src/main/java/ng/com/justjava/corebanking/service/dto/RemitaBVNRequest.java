package ng.com.justjava.corebanking.service.dto; ;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "requestReference",
    "bvn"
})
@Generated("jsonschema2pojo")
public class RemitaBVNRequest {

    @JsonProperty("requestReference")
    private String requestReference;
    @JsonProperty("bvn")
    private String bvn;

    @JsonProperty("requestReference")
    public String getRequestReference() {
        return requestReference;
    }

    @JsonProperty("requestReference")
    public void setRequestReference(String requestReference) {
        this.requestReference = requestReference;
    }

    @JsonProperty("bvn")
    public String getBvn() {
        return bvn;
    }

    @JsonProperty("bvn")
    public void setBvn(String bvn) {
        this.bvn = bvn;
    }

    @Override
    public String toString() {
        return "RemitaBVNRequest{" +
            "requestReference='" + requestReference + '\'' +
            ", bvn='" + bvn + '\'' +
            '}';
    }
}
