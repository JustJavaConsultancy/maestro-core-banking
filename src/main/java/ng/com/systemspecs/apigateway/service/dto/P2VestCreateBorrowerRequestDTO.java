package ng.com.systemspecs.apigateway.service.dto;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "dob",
    "bvn",
    "phone_number"
})
@Generated("jsonschema2pojo")
public class P2VestCreateBorrowerRequestDTO {

    @JsonProperty("dob")
    private String dob;
    @JsonProperty("bvn")
    private String bvn;
    @JsonProperty("phone_number")
    private String phone_number;

    @JsonProperty("dob")
    public String getDob() {
        return dob;
    }

    @JsonProperty("dob")
    public void setDob(String dob) {
        this.dob = dob;
    }

    @JsonProperty("bvn")
    public String getBvn() {
        return bvn;
    }

    @JsonProperty("bvn")
    public void setBvn(String bvn) {
        this.bvn = bvn;
    }

    @JsonProperty("phone_number")
    public String getPhone_number() {
        return phone_number;
    }

    @JsonProperty("phone_number")
    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    @Override
    public String toString() {
        return "P2VestCreateBorrowerRequestDTO{" +
            "dob='" + dob + '\'' +
            ", bvn='" + bvn + '\'' +
            ", phone_number='" + phone_number + '\'' +
            '}';
    }
}
