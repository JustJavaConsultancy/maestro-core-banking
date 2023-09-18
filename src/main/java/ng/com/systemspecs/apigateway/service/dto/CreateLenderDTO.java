package ng.com.systemspecs.apigateway.service.dto;

import ng.com.systemspecs.apigateway.domain.enumeration.LoanType;
import ng.com.systemspecs.apigateway.domain.enumeration.Tenure;
import ng.com.systemspecs.apigateway.util.ValidPassword;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A DTO for the {@link ng.com.systemspecs.apigateway.domain.Lender} entity.
 */
public class CreateLenderDTO implements Serializable {

    private Long id;

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

    @NotNull
    private Double rate;

    @NotNull
    private Tenure preferredTenure;

    private LoanType loanType;


    private Long profileId;

    private String profilePhoneNumber;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public Tenure getPreferredTenure() {
        return preferredTenure;
    }

    public void setPreferredTenure(Tenure preferredTenure) {
        this.preferredTenure = preferredTenure;
    }

    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }

    public String getProfilePhoneNumber() {
        return profilePhoneNumber;
    }

    public void setProfilePhoneNumber(String profilePhoneNumber) {
        this.profilePhoneNumber = profilePhoneNumber;
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

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
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

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getNin() {
        return nin;
    }

    public void setNin(String nin) {
        this.nin = nin;
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

    public LoanType getLenderType() {
        return loanType;
    }

    public void setLenderType(LoanType loanType) {
        this.loanType = loanType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CreateLenderDTO)) {
            return false;
        }

        return id != null && id.equals(((CreateLenderDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "CreateLenderDTO{" +
            "id=" + id +
            ", firstName='" + firstName + '\'' +
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
            ", rate=" + rate +
            ", preferredTenure=" + preferredTenure +
            ", profileId=" + profileId +
            ", profilePhoneNumber='" + profilePhoneNumber + '\'' +
            ", loanType='" + loanType + '\'' +
            '}';
    }
}
