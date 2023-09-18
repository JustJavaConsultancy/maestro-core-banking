package ng.com.justjava.corebanking.service.dto;

import com.fasterxml.jackson.annotation.*;

import javax.annotation.Generated;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "borrower_id",
    "borrower_country",
    "borrower_fullname",
    "borrower_firstname",
    "borrower_lastname",
    "borrower_business_name",
    "borrower_unique_number",
    "borrower_gender",
    "borrower_title",
    "borrower_mobile",
    "borrower_email",
    "borrower_dob",
    "borrower_address",
    "borrower_city",
    "borrower_province",
    "borrower_zipcode",
    "borrower_landline",
    "borrower_working_status",
    "borrower_credit_score",
    "borrower_description",
    "borrower_access_ids",
    "borrower_photo",
    "custom_field_5035",
    "custom_field_5037",
    "custom_field_5040"
})
@Generated("jsonschema2pojo")
public class CreateBorrowerDTO {

    @JsonProperty("borrower_id")
    private Long borrowerId;
    @JsonProperty("borrower_country")
    @NotEmpty(message = "Borrower Country cannot be null")
    private String borrowerCountry = "NG";
    @JsonProperty("borrower_fullname")
    private Object borrowerFullname;
    @JsonProperty("borrower_firstname")
    private String borrowerFirstname;
    @JsonProperty("borrower_lastname")
    private String borrowerLastname;
    @JsonProperty("borrower_business_name")
    private String borrowerBusinessName;
    @JsonProperty("borrower_unique_number")
    private String borrowerUniqueNumber;
    @JsonProperty("borrower_gender")
    private String borrowerGender;
    @JsonProperty("borrower_title")
    private Integer borrowerTitle;
    @JsonProperty("borrower_mobile")
    private String borrowerMobile;
    @JsonProperty("borrower_email")
    private String borrowerEmail;
    @JsonProperty("borrower_dob")
    private String borrowerDob;
    @JsonProperty("borrower_address")
    private String borrowerAddress;
    @JsonProperty("borrower_city")
    private String borrowerCity;
    @JsonProperty("borrower_province")
    private String borrowerProvince;
    @JsonProperty("borrower_zipcode")
    private String borrowerZipcode;
    @JsonProperty("borrower_landline")
    private String borrowerLandline;
    @JsonProperty("borrower_working_status")
    private String borrowerWorkingStatus;
    @JsonProperty("borrower_credit_score")
    private String borrowerCreditScore;
    @JsonProperty("borrower_description")
    private String borrowerDescription;
    @JsonProperty("borrower_access_ids")
    private List<Object> borrowerAccessIds = new ArrayList<>();
    @JsonProperty("borrower_photo")
    private Object borrowerPhoto;
    @JsonProperty("custom_field_5035")
    private String customField5035;
    @JsonProperty("custom_field_5037")
    private String customField5037;
    @JsonProperty("custom_field_5040")
    @NotEmpty(message = "Marital status cannot be empty")
    private String customField5040;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("borrower_id")
    public Long getBorrowerId() {
        return borrowerId;
    }

    @JsonProperty("borrower_id")
    public void setBorrowerId(Long borrowerId) {
        this.borrowerId = borrowerId;
    }

    @JsonProperty("borrower_country")
    public String getBorrowerCountry() {
        return borrowerCountry;
    }

    @JsonProperty("borrower_country")
    public void setBorrowerCountry(String borrowerCountry) {
        this.borrowerCountry = borrowerCountry;
    }

    @JsonProperty("borrower_fullname")
    public Object getBorrowerFullname() {
        return borrowerFullname;
    }

    @JsonProperty("borrower_fullname")
    public void setBorrowerFullname(Object borrowerFullname) {
        this.borrowerFullname = borrowerFullname;
    }

    @JsonProperty("borrower_firstname")
    public String getBorrowerFirstname() {
        return borrowerFirstname;
    }

    @JsonProperty("borrower_firstname")
    public void setBorrowerFirstname(String borrowerFirstname) {
        this.borrowerFirstname = borrowerFirstname;
    }

    @JsonProperty("borrower_lastname")
    public String getBorrowerLastname() {
        return borrowerLastname;
    }

    @JsonProperty("borrower_lastname")
    public void setBorrowerLastname(String borrowerLastname) {
        this.borrowerLastname = borrowerLastname;
    }

    @JsonProperty("borrower_business_name")
    public String getBorrowerBusinessName() {
        return borrowerBusinessName;
    }

    @JsonProperty("borrower_business_name")
    public void setBorrowerBusinessName(String borrowerBusinessName) {
        this.borrowerBusinessName = borrowerBusinessName;
    }

    @JsonProperty("borrower_unique_number")
    public String getBorrowerUniqueNumber() {
        return borrowerUniqueNumber;
    }

    @JsonProperty("borrower_unique_number")
    public void setBorrowerUniqueNumber(String borrowerUniqueNumber) {
        this.borrowerUniqueNumber = borrowerUniqueNumber;
    }

    @JsonProperty("borrower_gender")
    public String getBorrowerGender() {
        return borrowerGender;
    }

    @JsonProperty("borrower_gender")
    public void setBorrowerGender(String borrowerGender) {
        this.borrowerGender = borrowerGender;
    }

    @JsonProperty("borrower_title")
    public Integer getBorrowerTitle() {
        return borrowerTitle;
    }

    @JsonProperty("borrower_title")
    public void setBorrowerTitle(Integer borrowerTitle) {
        this.borrowerTitle = borrowerTitle;
    }

    @JsonProperty("borrower_mobile")
    public String getBorrowerMobile() {
        return borrowerMobile;
    }

    @JsonProperty("borrower_mobile")
    public void setBorrowerMobile(String borrowerMobile) {
        this.borrowerMobile = borrowerMobile;
    }

    @JsonProperty("borrower_email")
    public String getBorrowerEmail() {
        return borrowerEmail;
    }

    @JsonProperty("borrower_email")
    public void setBorrowerEmail(String borrowerEmail) {
        this.borrowerEmail = borrowerEmail;
    }

    @JsonProperty("borrower_dob")
    public String getBorrowerDob() {
        return borrowerDob;
    }

    @JsonProperty("borrower_dob")
    public void setBorrowerDob(String borrowerDob) {
        this.borrowerDob = borrowerDob;
    }

    @JsonProperty("borrower_address")
    public String getBorrowerAddress() {
        return borrowerAddress;
    }

    @JsonProperty("borrower_address")
    public void setBorrowerAddress(String borrowerAddress) {
        this.borrowerAddress = borrowerAddress;
    }

    @JsonProperty("borrower_city")
    public String getBorrowerCity() {
        return borrowerCity;
    }

    @JsonProperty("borrower_city")
    public void setBorrowerCity(String borrowerCity) {
        this.borrowerCity = borrowerCity;
    }

    @JsonProperty("borrower_province")
    public String getBorrowerProvince() {
        return borrowerProvince;
    }

    @JsonProperty("borrower_province")
    public void setBorrowerProvince(String borrowerProvince) {
        this.borrowerProvince = borrowerProvince;
    }

    @JsonProperty("borrower_zipcode")
    public String getBorrowerZipcode() {
        return borrowerZipcode;
    }

    @JsonProperty("borrower_zipcode")
    public void setBorrowerZipcode(String borrowerZipcode) {
        this.borrowerZipcode = borrowerZipcode;
    }

    @JsonProperty("borrower_landline")
    public String getBorrowerLandline() {
        return borrowerLandline;
    }

    @JsonProperty("borrower_landline")
    public void setBorrowerLandline(String borrowerLandline) {
        this.borrowerLandline = borrowerLandline;
    }

    @JsonProperty("borrower_working_status")
    public String getBorrowerWorkingStatus() {
        return borrowerWorkingStatus;
    }

    @JsonProperty("borrower_working_status")
    public void setBorrowerWorkingStatus(String borrowerWorkingStatus) {
        this.borrowerWorkingStatus = borrowerWorkingStatus;
    }

    @JsonProperty("borrower_credit_score")
    public String getBorrowerCreditScore() {
        return borrowerCreditScore;
    }

    @JsonProperty("borrower_credit_score")
    public void setBorrowerCreditScore(String borrowerCreditScore) {
        this.borrowerCreditScore = borrowerCreditScore;
    }

    @JsonProperty("borrower_description")
    public String getBorrowerDescription() {
        return borrowerDescription;
    }

    @JsonProperty("borrower_description")
    public void setBorrowerDescription(String borrowerDescription) {
        this.borrowerDescription = borrowerDescription;
    }

    @JsonProperty("borrower_access_ids")
    public List<Object> getBorrowerAccessIds() {
        return borrowerAccessIds;
    }

    @JsonProperty("borrower_access_ids")
    public void setBorrowerAccessIds(List<Object> borrowerAccessIds) {
        this.borrowerAccessIds = borrowerAccessIds;
    }

    @JsonProperty("borrower_photo")
    public Object getBorrowerPhoto() {
        return borrowerPhoto;
    }

    @JsonProperty("borrower_photo")
    public void setBorrowerPhoto(Object borrowerPhoto) {
        this.borrowerPhoto = borrowerPhoto;
    }

    @JsonProperty("custom_field_5035")
    public String getCustomField5035() {
        return customField5035;
    }

    @JsonProperty("custom_field_5035")
    public void setCustomField5035(String customField5035) {
        this.customField5035 = customField5035;
    }

    @JsonProperty("custom_field_5037")
    public String getCustomField5037() {
        return customField5037;
    }

    @JsonProperty("custom_field_5037")
    public void setCustomField5037(String customField5037) {
        this.customField5037 = customField5037;
    }

    @JsonProperty("custom_field_5040")
    public String getCustomField5040() {
        return customField5040;
    }

    @JsonProperty("custom_field_5040")
    public void setCustomField5040(String customField5040) {
        this.customField5040 = customField5040;
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
        return "CreateBorrowerDTO{" +
            "borrowerId=" + borrowerId +
            ", borrowerCountry='" + borrowerCountry + '\'' +
            ", borrowerFullname=" + borrowerFullname +
            ", borrowerFirstname='" + borrowerFirstname + '\'' +
            ", borrowerLastname='" + borrowerLastname + '\'' +
            ", borrowerBusinessName='" + borrowerBusinessName + '\'' +
            ", borrowerUniqueNumber='" + borrowerUniqueNumber + '\'' +
            ", borrowerGender='" + borrowerGender + '\'' +
            ", borrowerTitle=" + borrowerTitle +
            ", borrowerMobile='" + borrowerMobile + '\'' +
            ", borrowerEmail='" + borrowerEmail + '\'' +
            ", borrowerDob='" + borrowerDob + '\'' +
            ", borrowerAddress='" + borrowerAddress + '\'' +
            ", borrowerCity='" + borrowerCity + '\'' +
            ", borrowerProvince='" + borrowerProvince + '\'' +
            ", borrowerZipcode='" + borrowerZipcode + '\'' +
            ", borrowerLandline='" + borrowerLandline + '\'' +
            ", borrowerWorkingStatus='" + borrowerWorkingStatus + '\'' +
            ", borrowerCreditScore='" + borrowerCreditScore + '\'' +
            ", borrowerDescription='" + borrowerDescription + '\'' +
            ", borrowerAccessIds=" + borrowerAccessIds +
            ", borrowerPhoto=" + borrowerPhoto +
            ", customField5035='" + customField5035 + '\'' +
            ", customField5037='" + customField5037 + '\'' +
            ", customField5040='" + customField5040 + '\'' +
            ", additionalProperties=" + additionalProperties +
            '}';
    }
}
