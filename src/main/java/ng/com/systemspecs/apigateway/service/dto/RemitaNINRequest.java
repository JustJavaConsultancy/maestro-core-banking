package ng.com.systemspecs.apigateway.service.dto; ;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "number"
})
@Generated("jsonschema2pojo")
public class RemitaNINRequest {

    @JsonProperty("number")
    private String number;

    @JsonProperty("number")
    public String getNumber() {
        return number;
    }

    @JsonProperty("number")
    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "RemitaNINRequest{" +
            "number='" + number + '\'' +
            '}';
    }
}
