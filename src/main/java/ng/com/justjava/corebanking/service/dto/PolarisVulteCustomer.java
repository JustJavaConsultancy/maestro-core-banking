package ng.com.justjava.corebanking.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "customer_ref",
    "firstname",
    "surname",
    "email",
    "mobile_no"
})
public class PolarisVulteCustomer {

    @JsonProperty("customer_ref")
    private String customerRef;
    @JsonProperty("firstname")
    private String firstname;
    @JsonProperty("surname")
    private String surname;
    @JsonProperty("email")
    private String email;
    @JsonProperty("mobile_no")
    private String mobileNo;

    @JsonProperty("customer_ref")
    public String getCustomerRef() {
        return customerRef;
    }

    @JsonProperty("customer_ref")
    public void setCustomerRef(String customerRef) {
        this.customerRef = customerRef;
    }

    @JsonProperty("firstname")
    public String getFirstname() {
        return firstname;
    }

    @JsonProperty("firstname")
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    @JsonProperty("surname")
    public String getSurname() {
        return surname;
    }

    @JsonProperty("surname")
    public void setSurname(String surname) {
        this.surname = surname;
    }

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    @JsonProperty("email")
    public void setEmail(String email) {
        this.email = email;
    }

    @JsonProperty("mobile_no")
    public String getMobileNo() {
        return mobileNo;
    }

    @JsonProperty("mobile_no")
    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    @Override
    public String toString() {
        return "PolarisVulteCustomer{" +
            "customerRef='" + customerRef + '\'' +
            ", firstname='" + firstname + '\'' +
            ", surname='" + surname + '\'' +
            ", email='" + email + '\'' +
            ", mobileNo='" + mobileNo + '\'' +
            '}';
    }
}
