package ng.com.systemspecs.apigateway.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "firstName",
    "middleName",
    "lastName",
    "dateOfBirth",
    "phoneNumber",
    "registrationDate",
    "enrollmentBank",
    "enrollmentBranch",
    "imageBase64",
    "address",
    "male",
    "email",
    "watchList",
    "nationality",
    "maritalStatus",
    "stateOfResidence",
    "lgaOfResidence",
    "image",
    "gender"
})
@Generated("jsonschema2pojo")
public class GetBvnResponseData {

    @JsonProperty("firstName")
    private String firstName;
    @JsonProperty("middleName")
    private String middleName;
    @JsonProperty("lastName")
    private String lastName;
    @JsonProperty("dateOfBirth")
    private String dateOfBirth;
    @JsonProperty("phoneNumber")
    private String phoneNumber;
    @JsonProperty("registrationDate")
    private String registrationDate;
    @JsonProperty("enrollmentBank")
    private String enrollmentBank;
    @JsonProperty("enrollmentBranch")
    private String enrollmentBranch;
    @JsonProperty("imageBase64")
    private String imageBase64;
    @JsonProperty("address")
    private String address;
    @JsonProperty("male")
    private String male;
    @JsonProperty("email")
    private String email;
    @JsonProperty("watchList")
    private String watchList;
    @JsonProperty("nationality")
    private String nationality;
    @JsonProperty("maritalStatus")
    private String maritalStatus;
    @JsonProperty("stateOfResidence")
    private String stateOfResidence;
    @JsonProperty("lgaOfResidence")
    private String lgaOfResidence;
    @JsonProperty("image")
    private String image;
    @JsonProperty("gender")
    private String gender;

    @JsonProperty("firstName")
    public String getFirstName() {
        return firstName;
    }

    @JsonProperty("firstName")
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @JsonProperty("middleName")
    public String getMiddleName() {
        return middleName;
    }

    @JsonProperty("middleName")
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    @JsonProperty("lastName")
    public String getLastName() {
        return lastName;
    }

    @JsonProperty("lastName")
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @JsonProperty("dateOfBirth")
    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @JsonProperty("dateOfBirth")
    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @JsonProperty("phoneNumber")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @JsonProperty("phoneNumber")
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @JsonProperty("registrationDate")
    public String getRegistrationDate() {
        return registrationDate;
    }

    @JsonProperty("registrationDate")
    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    @JsonProperty("enrollmentBank")
    public String getEnrollmentBank() {
        return enrollmentBank;
    }

    @JsonProperty("enrollmentBank")
    public void setEnrollmentBank(String enrollmentBank) {
        this.enrollmentBank = enrollmentBank;
    }

    @JsonProperty("enrollmentBranch")
    public String getEnrollmentBranch() {
        return enrollmentBranch;
    }

    @JsonProperty("enrollmentBranch")
    public void setEnrollmentBranch(String enrollmentBranch) {
        this.enrollmentBranch = enrollmentBranch;
    }

    @JsonProperty("imageBase64")
    public String getImageBase64() {
        return imageBase64;
    }

    @JsonProperty("imageBase64")
    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

    @JsonProperty("address")
    public String getAddress() {
        return address;
    }

    @JsonProperty("address")
    public void setAddress(String address) {
        this.address = address;
    }

    @JsonProperty("male")
    public String getMale() {
        return male;
    }

    @JsonProperty("male")
    public void setMale(String male) {
        this.male = male;
    }

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    @JsonProperty("email")
    public void setEmail(String email) {
        this.email = email;
    }

    @JsonProperty("watchList")
    public String getWatchList() {
        return watchList;
    }

    @JsonProperty("watchList")
    public void setWatchList(String watchList) {
        this.watchList = watchList;
    }

    @JsonProperty("nationality")
    public String getNationality() {
        return nationality;
    }

    @JsonProperty("nationality")
    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    @JsonProperty("maritalStatus")
    public String getMaritalStatus() {
        return maritalStatus;
    }

    @JsonProperty("maritalStatus")
    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    @JsonProperty("stateOfResidence")
    public String getStateOfResidence() {
        return stateOfResidence;
    }

    @JsonProperty("stateOfResidence")
    public void setStateOfResidence(String stateOfResidence) {
        this.stateOfResidence = stateOfResidence;
    }

    @JsonProperty("lgaOfResidence")
    public String getLgaOfResidence() {
        return lgaOfResidence;
    }

    @JsonProperty("lgaOfResidence")
    public void setLgaOfResidence(String lgaOfResidence) {
        this.lgaOfResidence = lgaOfResidence;
    }

    @JsonProperty("image")
    public String getImage() {
        return image;
    }

    @JsonProperty("image")
    public void setImage(String image) {
        this.image = image;
    }


    @Override
    public String toString() {
        return "GetBvnResponseData{" +
            "firstName='" + firstName + '\'' +
            ", middleName='" + middleName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", dateOfBirth='" + dateOfBirth + '\'' +
            ", phoneNumber='" + phoneNumber + '\'' +
            ", registrationDate='" + registrationDate + '\'' +
            ", enrollmentBank='" + enrollmentBank + '\'' +
            ", enrollmentBranch='" + enrollmentBranch + '\'' +
            ", imageBase64='" + imageBase64 + '\'' +
            ", address='" + address + '\'' +
            ", male='" + male + '\'' +
            ", email='" + email + '\'' +
            ", watchList='" + watchList + '\'' +
            ", nationality='" + nationality + '\'' +
            ", maritalStatus='" + maritalStatus + '\'' +
            ", stateOfResidence='" + stateOfResidence + '\'' +
            ", lgaOfResidence='" + lgaOfResidence + '\'' +
            ", image='" + image + '\'' +
            ", gender='" + gender + '\'' +
            '}';
    }
}
