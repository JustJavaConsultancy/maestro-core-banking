package ng.com.justjava.corebanking.service.dto;

import ng.com.justjava.corebanking.util.ValidPassword;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDate;

public class CreateAgentDTO {

    @NotEmpty(message = "firstname cannot be empty")
    private String firstName;

    @NotEmpty(message = "firstname cannot be empty")
    private String lastName;

    @Size(min = 11, max = 15, message = "Invalid phone number")
    private String phoneNumber;

    @ValidPassword(message = "Invalid password, please input a strong password")
    private String password;
    private String deviceNotificationToken;

    private String pin;
    private String scheme;

    private LocalDate dateOfBirth;
    private String gender;
    private String photo;
    private String state;
    private String occupation;
    private String localGovt;
    private Double latitude;
    private Double longitude;
    private String address;
    private String city;
    private String nin;
    //private Profile addressOwner;

    @Email
    private String email;
    private double openingBalance;
    private String accountName;
    private String accountNumber;
    private String location;
    private String locationLatitude;
    private String locationLongitude;


    private String businessName;
    private String businessType;
    private String companyRegType;
    private String companyRegNo;

    private String superAgentPhoneNumber;
    private Double walletLimit;
    private String bvn;

    private String cACDoc;
    private String cACDocFormat;
    private String c07Doc;
    private String c07DocFormat;
    private String c01Doc;
    private String c01DocFormat;
    private String c02Doc;
    private String c02DocFormat;
    private boolean isSuperAgent = false;

    public String getBvn() {
        return bvn;
    }

    public void setBvn(String bvn) {
        this.bvn = bvn;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDeviceNotificationToken() {
        return deviceNotificationToken;
    }

    public void setDeviceNotificationToken(String deviceNotificationToken) {
        this.deviceNotificationToken = deviceNotificationToken;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getLocalGovt() {
        return localGovt;
    }

    public void setLocalGovt(String localGovt) {
        this.localGovt = localGovt;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getOpeningBalance() {
        return openingBalance;
    }

    public void setOpeningBalance(double openingBalance) {
        this.openingBalance = openingBalance;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocationLatitude() {
        return locationLatitude;
    }

    public void setLocationLatitude(String locationLatitude) {
        this.locationLatitude = locationLatitude;
    }

    public String getLocationLongitude() {
        return locationLongitude;
    }

    public void setLocationLongitude(String locationLongitude) {
        this.locationLongitude = locationLongitude;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getNin() {
        return nin;
    }

    public void setNin(String nin) {
        this.nin = nin;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getCompanyRegType() {
        return companyRegType;
    }

    public void setCompanyRegType(String companyRegType) {
        this.companyRegType = companyRegType;
    }

    public String getCompanyRegNo() {
        return companyRegNo;
    }

    public void setCompanyRegNo(String companyRegNo) {
        this.companyRegNo = companyRegNo;
    }

    public String getSuperAgentPhoneNumber() {
        return superAgentPhoneNumber;
    }

    public void setSuperAgentPhoneNumber(String superAgentPhoneNumber) {
        this.superAgentPhoneNumber = superAgentPhoneNumber;
    }

    public String getcACDocFormat() {
        return cACDocFormat;
    }

    public void setcACDocFormat(String cACDocFormat) {
        this.cACDocFormat = cACDocFormat;
    }

    public String getC07DocFormat() {
        return c07DocFormat;
    }

    public void setC07DocFormat(String c07DocFormat) {
        this.c07DocFormat = c07DocFormat;
    }

    public String getC01DocFormat() {
        return c01DocFormat;
    }

    public void setC01DocFormat(String c01DocFormat) {
        this.c01DocFormat = c01DocFormat;
    }

    public String getC02DocFormat() {
        return c02DocFormat;
    }

    public void setC02DocFormat(String c02DocFormat) {
        this.c02DocFormat = c02DocFormat;
    }

    public String getcACDoc() {
        return cACDoc;
    }

    public void setcACDoc(String cACDoc) {
        this.cACDoc = cACDoc;
    }

    public String getC07Doc() {
        return c07Doc;
    }

    public void setC07Doc(String c07Doc) {
        this.c07Doc = c07Doc;
    }

    public String getC01Doc() {
        return c01Doc;
    }

    public void setC01Doc(String c01Doc) {
        this.c01Doc = c01Doc;
    }

    public String getC02Doc() {
        return c02Doc;
    }

    public void setC02Doc(String c02Doc) {
        this.c02Doc = c02Doc;
    }

    public boolean isSuperAgent() {
        return isSuperAgent;
    }

    public void setSuperAgent(boolean superAgent) {
        isSuperAgent = superAgent;
    }

    @Override
    public String toString() {
        return "CreateAgentDTO{" +
            "firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", phoneNumber='" + phoneNumber + '\'' +
            ", password='" + password + '\'' +
            ", deviceNotificationToken='" + deviceNotificationToken + '\'' +
            ", pin='" + pin + '\'' +
            ", scheme='" + scheme + '\'' +
            ", dateOfBirth=" + dateOfBirth +
            ", gender='" + gender + '\'' +
            ", photo='" + photo + '\'' +
            ", state='" + state + '\'' +
            ", occupation='" + occupation + '\'' +
            ", localGovt='" + localGovt + '\'' +
            ", latitude=" + latitude +
            ", longitude=" + longitude +
            ", address='" + address + '\'' +
            ", city='" + city + '\'' +
            ", nin='" + nin + '\'' +
            ", email='" + email + '\'' +
            ", openingBalance=" + openingBalance +
            ", accountName='" + accountName + '\'' +
            ", accountNumber='" + accountNumber + '\'' +
            ", location='" + location + '\'' +
            ", locationLatitude='" + locationLatitude + '\'' +
            ", locationLongitude='" + locationLongitude + '\'' +
            ", businessName='" + businessName + '\'' +
            ", businessType='" + businessType + '\'' +
            ", companyRegType='" + companyRegType + '\'' +
            ", companyRegNo='" + companyRegNo + '\'' +
            ", superAgentPhoneNumber='" + superAgentPhoneNumber + '\'' +
            ", walletLimit=" + walletLimit +
            ", cACDoc='" + cACDoc + '\'' +
            ", c07Doc='" + c07Doc + '\'' +
            ", c01Doc='" + c01Doc + '\'' +
            ", c02Doc='" + c02Doc + '\'' +
            ", c02Doc='" + c02Doc + '\'' +
            ", isSuperAgent='" + isSuperAgent + '\'' +
            ", bvn='" + bvn + '\'' +
            '}';
    }

    public Double getWalletLimit() {
        if (walletLimit == null) {
            return 0.0;
        }
        return walletLimit;
    }

    public void setWalletLimit(Double walletLimit) {
        this.walletLimit = walletLimit;
    }
}
