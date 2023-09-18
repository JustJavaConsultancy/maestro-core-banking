package ng.com.justjava.corebanking.service.dto;

import com.fasterxml.jackson.annotation.*;

import javax.annotation.Generated;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "TransactionTrackingRef",
    "accountType",
    "isControlAccount",
    "fullname",
    "othernames",
    "lastname",
    "Email",
    "Gender",
    "PhoneNo",
    "PlaceOfBirth",
    "DateOfBirth",
    "Address",
    "NationalIdentityNo",
    "NextOfKinPhoneNo",
    "NextOfKinName",
    "bvn",
    "state"
})
@Generated("jsonschema2pojo")
public class CashConnectAccountRequestDTO {

    @JsonProperty("TransactionTrackingRef")
    private String transactionTrackingRef;
    @JsonProperty("accountType")
    private String accountType;
    @JsonProperty("isControlAccount")
    private Integer isControlAccount;
    @JsonProperty("fullname")
    private String fullname;
    @JsonProperty("othernames")
    private String othernames;
    @JsonProperty("lastname")
    private String lastname;
    @JsonProperty("Email")
    private String email;
    @JsonProperty("Gender")
    private Integer gender;
    @JsonProperty("PhoneNo")
    private String phoneNo;
    @JsonProperty("PlaceOfBirth")
    private String placeOfBirth;
    @JsonProperty("DateOfBirth")
    private String dateOfBirth;
    @JsonProperty("Address")
    private String address;
    @JsonProperty("NationalIdentityNo")
    private String nationalIdentityNo;
    @JsonProperty("NextOfKinPhoneNo")
    private String nextOfKinPhoneNo;
    @JsonProperty("NextOfKinName")
    private String nextOfKinName;
    @JsonProperty("bvn")
    private String bvn;
    @JsonProperty("state")
    private String state;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("TransactionTrackingRef")
    public String getTransactionTrackingRef() {
        return transactionTrackingRef;
    }

    @JsonProperty("TransactionTrackingRef")
    public void setTransactionTrackingRef(String transactionTrackingRef) {
        this.transactionTrackingRef = transactionTrackingRef;
    }

    @JsonProperty("accountType")
    public String getAccountType() {
        return accountType;
    }

    @JsonProperty("accountType")
    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    @JsonProperty("isControlAccount")
    public Integer getIsControlAccount() {
        return isControlAccount;
    }

    @JsonProperty("isControlAccount")
    public void setIsControlAccount(Integer isControlAccount) {
        this.isControlAccount = isControlAccount;
    }

    @JsonProperty("fullname")
    public String getFullname() {
        return fullname;
    }

    @JsonProperty("fullname")
    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    @JsonProperty("othernames")
    public String getOthernames() {
        return othernames;
    }

    @JsonProperty("othernames")
    public void setOthernames(String othernames) {
        this.othernames = othernames;
    }

    @JsonProperty("lastname")
    public String getLastname() {
        return lastname;
    }

    @JsonProperty("lastname")
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    @JsonProperty("Email")
    public String getEmail() {
        return email;
    }

    @JsonProperty("Email")
    public void setEmail(String email) {
        this.email = email;
    }

    @JsonProperty("Gender")
    public Integer getGender() {
        return gender;
    }

    @JsonProperty("Gender")
    public void setGender(Integer gender) {
        this.gender = gender;
    }

    @JsonProperty("PhoneNo")
    public String getPhoneNo() {
        return phoneNo;
    }

    @JsonProperty("PhoneNo")
    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    @JsonProperty("PlaceOfBirth")
    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    @JsonProperty("PlaceOfBirth")
    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    @JsonProperty("DateOfBirth")
    public String getDateOfBirth() {
        return dateOfBirth;
    }

    @JsonProperty("DateOfBirth")
    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @JsonProperty("Address")
    public String getAddress() {
        return address;
    }

    @JsonProperty("Address")
    public void setAddress(String address) {
        this.address = address;
    }

    @JsonProperty("NationalIdentityNo")
    public String getNationalIdentityNo() {
        return nationalIdentityNo;
    }

    @JsonProperty("NationalIdentityNo")
    public void setNationalIdentityNo(String nationalIdentityNo) {
        this.nationalIdentityNo = nationalIdentityNo;
    }

    @JsonProperty("NextOfKinPhoneNo")
    public String getNextOfKinPhoneNo() {
        return nextOfKinPhoneNo;
    }

    @JsonProperty("NextOfKinPhoneNo")
    public void setNextOfKinPhoneNo(String nextOfKinPhoneNo) {
        this.nextOfKinPhoneNo = nextOfKinPhoneNo;
    }

    @JsonProperty("NextOfKinName")
    public String getNextOfKinName() {
        return nextOfKinName;
    }

    @JsonProperty("NextOfKinName")
    public void setNextOfKinName(String nextOfKinName) {
        this.nextOfKinName = nextOfKinName;
    }

    @JsonProperty("bvn")
    public String getBvn() {
        return bvn;
    }

    @JsonProperty("bvn")
    public void setBvn(String bvn) {
        this.bvn = bvn;
    }

    @JsonProperty("state")
    public String getState() {
        return state;
    }

    @JsonProperty("state")
    public void setState(String state) {
        this.state = state;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public String toString() {
        return "CashConnectAccountRequestDTO{" +
            "transactionTrackingRef='" + transactionTrackingRef + '\'' +
            ", accountType='" + accountType + '\'' +
            ", isControlAccount=" + isControlAccount +
            ", fullname='" + fullname + '\'' +
            ", othernames='" + othernames + '\'' +
            ", lastname='" + lastname + '\'' +
            ", email='" + email + '\'' +
            ", gender=" + gender +
            ", phoneNo='" + phoneNo + '\'' +
            ", placeOfBirth='" + placeOfBirth + '\'' +
            ", dateOfBirth='" + dateOfBirth + '\'' +
            ", address='" + address + '\'' +
            ", nationalIdentityNo='" + nationalIdentityNo + '\'' +
            ", nextOfKinPhoneNo='" + nextOfKinPhoneNo + '\'' +
            ", nextOfKinName='" + nextOfKinName + '\'' +
            ", bvn='" + bvn + '\'' +
            ", state='" + state + '\'' +
            ", additionalProperties=" + additionalProperties +
            '}';
    }
}
