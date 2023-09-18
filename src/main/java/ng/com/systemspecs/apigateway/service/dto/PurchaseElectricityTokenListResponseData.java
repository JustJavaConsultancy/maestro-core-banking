package ng.com.systemspecs.apigateway.service.dto;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "token1",
    "token2",
    "token1_desc",
    "token2_desc",
    "token3_desc"
})
@Generated("jsonschema2pojo")
public class PurchaseElectricityTokenListResponseData {

    @JsonProperty("token1")
    private String token1;
    @JsonProperty("token2")
    private Object token2;
    @JsonProperty("token1_desc")
    private String token1_desc;
    @JsonProperty("token2_desc")
    private String token2_desc;
    @JsonProperty("token3_desc")
    private String token3_desc;

    @JsonProperty("token1")
    public String getToken1() {
        return token1;
    }

    @JsonProperty("token1")
    public void setToken1(String token1) {
        this.token1 = token1;
    }

    @JsonProperty("token2")
    public Object getToken2() {
        return token2;
    }

    @JsonProperty("token2")
    public void setToken2(Object token2) {
        this.token2 = token2;
    }

    @JsonProperty("token1_desc")
    public String getToken1_desc() {
        return token1_desc;
    }

    @JsonProperty("token1_desc")
    public void setToken1_desc(String token1_desc) {
        this.token1_desc = token1_desc;
    }

    @JsonProperty("token2_desc")
    public String getToken2_desc() {
        return token2_desc;
    }

    @JsonProperty("token2_desc")
    public void setToken2_desc(String token2_desc) {
        this.token2_desc = token2_desc;
    }

    @JsonProperty("token3_desc")
    public String getToken3_desc() {
        return token3_desc;
    }

    @JsonProperty("token3_desc")
    public void setToken3_desc(String token3_desc) {
        this.token3_desc = token3_desc;
    }

    @Override
    public String toString() {
        return "TokenList{" +
            "token1='" + token1 + '\'' +
            ", token2=" + token2 +
            ", token1_desc='" + token1_desc + '\'' +
            ", token2_desc='" + token2_desc + '\'' +
            ", token3_desc='" + token3_desc + '\'' +
            '}';
    }
}
