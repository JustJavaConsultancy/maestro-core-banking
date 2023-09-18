package ng.com.justjava.corebanking.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "name_on_account",
    "middlename",
    "dob",
    "gender",
    "title",
    "address_line_1",
    "address_line_2",
    "city",
    "state",
    "country"
})
@Generated("jsonschema2pojo")
public class PolarisVulteOpenAccountDetails {

    @JsonProperty("name_on_account")
    private String nameOnAccount;
    @JsonProperty("middlename")
    private String middlename;
    @JsonProperty("dob")
    private String dob;
    @JsonProperty("gender")
    private String gender;
    @JsonProperty("title")
    private String title;
    @JsonProperty("address_line_1")
    private String addressLine1;
    @JsonProperty("address_line_2")
    private String addressLine2;
    @JsonProperty("city")
    private String city;
    @JsonProperty("state")
    private String state;
    @JsonProperty("country")
    private String country;

    @JsonProperty("name_on_account")
    public String getNameOnAccount() {
        return nameOnAccount;
    }

    @JsonProperty("name_on_account")
    public void setNameOnAccount(String nameOnAccount) {
        this.nameOnAccount = nameOnAccount;
    }

    @JsonProperty("middlename")
    public String getMiddlename() {
        return middlename;
    }

    @JsonProperty("middlename")
    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }

    @JsonProperty("dob")
    public String getDob() {
        return dob;
    }

    @JsonProperty("dob")
    public void setDob(String dob) {
        this.dob = dob;
    }

    @JsonProperty("gender")
    public String getGender() {
        return gender;
    }

    @JsonProperty("gender")
    public void setGender(String gender) {
        this.gender = gender;
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    @JsonProperty("address_line_1")
    public String getAddressLine1() {
        return addressLine1;
    }

    @JsonProperty("address_line_1")
    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    @JsonProperty("address_line_2")
    public String getAddressLine2() {
        return addressLine2;
    }

    @JsonProperty("address_line_2")
    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    @JsonProperty("city")
    public String getCity() {
        return city;
    }

    @JsonProperty("city")
    public void setCity(String city) {
        this.city = city;
    }

    @JsonProperty("state")
    public String getState() {
        return state;
    }

    @JsonProperty("state")
    public void setState(String state) {
        this.state = state;
    }

    @JsonProperty("country")
    public String getCountry() {
        return country;
    }

    @JsonProperty("country")
    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "PolarisVulteOpenAccountDetails{" +
            "nameOnAccount='" + nameOnAccount + '\'' +
            ", middlename='" + middlename + '\'' +
            ", dob='" + dob + '\'' +
            ", gender='" + gender + '\'' +
            ", title='" + title + '\'' +
            ", addressLine1='" + addressLine1 + '\'' +
            ", addressLine2='" + addressLine2 + '\'' +
            ", city='" + city + '\'' +
            ", state='" + state + '\'' +
            ", country='" + country + '\'' +
            '}';
    }
}
