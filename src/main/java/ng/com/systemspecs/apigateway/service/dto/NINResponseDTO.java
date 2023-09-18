package ng.com.systemspecs.apigateway.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "poBox",
    "biometric",
    "email",
    "address",
    "staffId",
    "villageTownCity",
    "dateCreated",
    "gender",
    "stateOfOriginCode",
    "stateOfResidenceCode",
    "dateOfBirth",
    "designation",
    "firstName",
    "zipCode",
    "employer",
    "lgaOfResidenceCode",
    "houseName",
    "title",
    "countryOfResidenceCode",
    "middleName",
    "nationality",
    "maritalStatus",
    "nextOfKin",
    "mobileNumber",
    "nin",
    "pin",
    "lastName",
    "bvn",
    "transactionRef",
    "photo",
    "signature",
    "countryOfBirth",
    "birthState",
    "employmentStatus",
    "languageSpoken",
    "profession",
    "lgaofOriginCode"
})
@Generated("jsonschema2pojo")
public class NINResponseDTO {

    @JsonProperty("poBox")
    private Object poBox;
    @JsonProperty("biometric")
    private Object biometric;
    @JsonProperty("email")
    private String email;
    @JsonProperty("address")
    private String address;
    @JsonProperty("staffId")
    private Object staffId;
    @JsonProperty("villageTownCity")
    private Object villageTownCity;
    @JsonProperty("dateCreated")
    private Object dateCreated;
    @JsonProperty("gender")
    private String gender;
    @JsonProperty("stateOfOriginCode")
    private String stateOfOriginCode;
    @JsonProperty("stateOfResidenceCode")
    private String stateOfResidenceCode;
    @JsonProperty("dateOfBirth")
    private String dateOfBirth;
    @JsonProperty("designation")
    private Object designation;
    @JsonProperty("firstName")
    private String firstName;
    @JsonProperty("zipCode")
    private Object zipCode;
    @JsonProperty("employer")
    private Object employer;
    @JsonProperty("lgaOfResidenceCode")
    private String lgaOfResidenceCode;
    @JsonProperty("houseName")
    private Object houseName;
    @JsonProperty("title")
    private String title;
    @JsonProperty("countryOfResidenceCode")
    private Object countryOfResidenceCode;
    @JsonProperty("middleName")
    private String middleName;
    @JsonProperty("nationality")
    private String nationality;
    @JsonProperty("maritalStatus")
    private String maritalStatus;
    @JsonProperty("nextOfKin")
    private String nextOfKin;
    @JsonProperty("mobileNumber")
    private String mobileNumber;
    @JsonProperty("nin")
    private String nin;
    @JsonProperty("pin")
    private Object pin;
    @JsonProperty("lastName")
    private String lastName;
    @JsonProperty("bvn")
    private Object bvn;
    @JsonProperty("transactionRef")
    private Object transactionRef;
    @JsonProperty("photo")
    private String photo;
    @JsonProperty("signature")
    private String signature;
    @JsonProperty("countryOfBirth")
    private String countryOfBirth;
    @JsonProperty("birthState")
    private String birthState;
    @JsonProperty("employmentStatus")
    private String employmentStatus;
    @JsonProperty("languageSpoken")
    private String languageSpoken;
    @JsonProperty("profession")
    private String profession;
    @JsonProperty("lgaofOriginCode")
    private String lgaofOriginCode;

    @JsonProperty("poBox")
    public Object getPoBox() {
        return poBox;
    }

    @JsonProperty("poBox")
    public void setPoBox(Object poBox) {
        this.poBox = poBox;
    }

    @JsonProperty("biometric")
    public Object getBiometric() {
        return biometric;
    }

    @JsonProperty("biometric")
    public void setBiometric(Object biometric) {
        this.biometric = biometric;
    }

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    @JsonProperty("email")
    public void setEmail(String email) {
        this.email = email;
    }

    @JsonProperty("address")
    public String getAddress() {
        return address;
    }

    @JsonProperty("address")
    public void setAddress(String address) {
        this.address = address;
    }

    @JsonProperty("staffId")
    public Object getStaffId() {
        return staffId;
    }

    @JsonProperty("staffId")
    public void setStaffId(Object staffId) {
        this.staffId = staffId;
    }

    @JsonProperty("villageTownCity")
    public Object getVillageTownCity() {
        return villageTownCity;
    }

    @JsonProperty("villageTownCity")
    public void setVillageTownCity(Object villageTownCity) {
        this.villageTownCity = villageTownCity;
    }

    @JsonProperty("dateCreated")
    public Object getDateCreated() {
        return dateCreated;
    }

    @JsonProperty("dateCreated")
    public void setDateCreated(Object dateCreated) {
        this.dateCreated = dateCreated;
    }

    @JsonProperty("gender")
    public String getGender() {
        return gender;
    }

    @JsonProperty("gender")
    public void setGender(String gender) {
        this.gender = gender;
    }

    @JsonProperty("stateOfOriginCode")
    public String getStateOfOriginCode() {
        return stateOfOriginCode;
    }

    @JsonProperty("stateOfOriginCode")
    public void setStateOfOriginCode(String stateOfOriginCode) {
        this.stateOfOriginCode = stateOfOriginCode;
    }

    @JsonProperty("stateOfResidenceCode")
    public String getStateOfResidenceCode() {
        return stateOfResidenceCode;
    }

    @JsonProperty("stateOfResidenceCode")
    public void setStateOfResidenceCode(String stateOfResidenceCode) {
        this.stateOfResidenceCode = stateOfResidenceCode;
    }

    @JsonProperty("dateOfBirth")
    public String getDateOfBirth() {
        return dateOfBirth;
    }

    @JsonProperty("dateOfBirth")
    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @JsonProperty("designation")
    public Object getDesignation() {
        return designation;
    }

    @JsonProperty("designation")
    public void setDesignation(Object designation) {
        this.designation = designation;
    }

    @JsonProperty("firstName")
    public String getFirstName() {
        return firstName;
    }

    @JsonProperty("firstName")
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @JsonProperty("zipCode")
    public Object getZipCode() {
        return zipCode;
    }

    @JsonProperty("zipCode")
    public void setZipCode(Object zipCode) {
        this.zipCode = zipCode;
    }

    @JsonProperty("employer")
    public Object getEmployer() {
        return employer;
    }

    @JsonProperty("employer")
    public void setEmployer(Object employer) {
        this.employer = employer;
    }

    @JsonProperty("lgaOfResidenceCode")
    public String getLgaOfResidenceCode() {
        return lgaOfResidenceCode;
    }

    @JsonProperty("lgaOfResidenceCode")
    public void setLgaOfResidenceCode(String lgaOfResidenceCode) {
        this.lgaOfResidenceCode = lgaOfResidenceCode;
    }

    @JsonProperty("houseName")
    public Object getHouseName() {
        return houseName;
    }

    @JsonProperty("houseName")
    public void setHouseName(Object houseName) {
        this.houseName = houseName;
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    @JsonProperty("countryOfResidenceCode")
    public Object getCountryOfResidenceCode() {
        return countryOfResidenceCode;
    }

    @JsonProperty("countryOfResidenceCode")
    public void setCountryOfResidenceCode(Object countryOfResidenceCode) {
        this.countryOfResidenceCode = countryOfResidenceCode;
    }

    @JsonProperty("middleName")
    public String getMiddleName() {
        return middleName;
    }

    @JsonProperty("middleName")
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
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

    @JsonProperty("nextOfKin")
    public String getNextOfKin() {
        return nextOfKin;
    }

    @JsonProperty("nextOfKin")
    public void setNextOfKin(String nextOfKin) {
        this.nextOfKin = nextOfKin;
    }

    @JsonProperty("mobileNumber")
    public String getMobileNumber() {
        return mobileNumber;
    }

    @JsonProperty("mobileNumber")
    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    @JsonProperty("nin")
    public String getNin() {
        return nin;
    }

    @JsonProperty("nin")
    public void setNin(String nin) {
        this.nin = nin;
    }

    @JsonProperty("pin")
    public Object getPin() {
        return pin;
    }

    @JsonProperty("pin")
    public void setPin(Object pin) {
        this.pin = pin;
    }

    @JsonProperty("lastName")
    public String getLastName() {
        return lastName;
    }

    @JsonProperty("lastName")
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @JsonProperty("bvn")
    public Object getBvn() {
        return bvn;
    }

    @JsonProperty("bvn")
    public void setBvn(Object bvn) {
        this.bvn = bvn;
    }

    @JsonProperty("transactionRef")
    public Object getTransactionRef() {
        return transactionRef;
    }

    @JsonProperty("transactionRef")
    public void setTransactionRef(Object transactionRef) {
        this.transactionRef = transactionRef;
    }

    @JsonProperty("photo")
    public String getPhoto() {
        return photo;
    }

    @JsonProperty("photo")
    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @JsonProperty("signature")
    public String getSignature() {
        return signature;
    }

    @JsonProperty("signature")
    public void setSignature(String signature) {
        this.signature = signature;
    }

    @JsonProperty("countryOfBirth")
    public String getCountryOfBirth() {
        return countryOfBirth;
    }

    @JsonProperty("countryOfBirth")
    public void setCountryOfBirth(String countryOfBirth) {
        this.countryOfBirth = countryOfBirth;
    }

    @JsonProperty("birthState")
    public String getBirthState() {
        return birthState;
    }

    @JsonProperty("birthState")
    public void setBirthState(String birthState) {
        this.birthState = birthState;
    }

    @JsonProperty("employmentStatus")
    public String getEmploymentStatus() {
        return employmentStatus;
    }

    @JsonProperty("employmentStatus")
    public void setEmploymentStatus(String employmentStatus) {
        this.employmentStatus = employmentStatus;
    }

    @JsonProperty("languageSpoken")
    public String getLanguageSpoken() {
        return languageSpoken;
    }

    @JsonProperty("languageSpoken")
    public void setLanguageSpoken(String languageSpoken) {
        this.languageSpoken = languageSpoken;
    }

    @JsonProperty("profession")
    public String getProfession() {
        return profession;
    }

    @JsonProperty("profession")
    public void setProfession(String profession) {
        this.profession = profession;
    }

    @JsonProperty("lgaofOriginCode")
    public String getLgaofOriginCode() {
        return lgaofOriginCode;
    }

    @JsonProperty("lgaofOriginCode")
    public void setLgaofOriginCode(String lgaofOriginCode) {
        this.lgaofOriginCode = lgaofOriginCode;
    }

    @Override
    public String toString() {
        return "NINResponseDTO{" +
            "poBox=" + poBox +
            ", biometric=" + biometric +
            ", email='" + email + '\'' +
            ", address='" + address + '\'' +
            ", staffId=" + staffId +
            ", villageTownCity=" + villageTownCity +
            ", dateCreated=" + dateCreated +
            ", gender='" + gender + '\'' +
            ", stateOfOriginCode='" + stateOfOriginCode + '\'' +
            ", stateOfResidenceCode='" + stateOfResidenceCode + '\'' +
            ", dateOfBirth='" + dateOfBirth + '\'' +
            ", designation=" + designation +
            ", firstName='" + firstName + '\'' +
            ", zipCode=" + zipCode +
            ", employer=" + employer +
            ", lgaOfResidenceCode='" + lgaOfResidenceCode + '\'' +
            ", houseName=" + houseName +
            ", title='" + title + '\'' +
            ", countryOfResidenceCode=" + countryOfResidenceCode +
            ", middleName='" + middleName + '\'' +
            ", nationality='" + nationality + '\'' +
            ", maritalStatus='" + maritalStatus + '\'' +
            ", nextOfKin='" + nextOfKin + '\'' +
            ", mobileNumber='" + mobileNumber + '\'' +
            ", nin='" + nin + '\'' +
            ", pin=" + pin +
            ", lastName='" + lastName + '\'' +
            ", bvn=" + bvn +
            ", transactionRef=" + transactionRef +
            ", photo='" + photo + '\'' +
            ", signature='" + signature + '\'' +
            ", countryOfBirth='" + countryOfBirth + '\'' +
            ", birthState='" + birthState + '\'' +
            ", employmentStatus='" + employmentStatus + '\'' +
            ", languageSpoken='" + languageSpoken + '\'' +
            ", profession='" + profession + '\'' +
            ", lgaofOriginCode='" + lgaofOriginCode + '\'' +
            '}';
    }
}
