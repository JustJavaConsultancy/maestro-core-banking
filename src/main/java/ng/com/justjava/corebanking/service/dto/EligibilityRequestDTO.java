
package ng.com.justjava.corebanking.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "phoneNumber"
})
@Generated("jsonschema2pojo")
public class EligibilityRequestDTO {

    @JsonProperty("phoneNumber")
    private String phoneNumber;

    @JsonProperty("loanId")
    private String loanId;

    @JsonProperty("phoneNumber")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @JsonProperty("phoneNumber")
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    @Override
    public String toString() {
        return "EligibilityRequestDTO{" +
            "phoneNumber='" + phoneNumber + '\'' +
            "loanId='" + loanId + '\'' +
            '}';
    }
}
