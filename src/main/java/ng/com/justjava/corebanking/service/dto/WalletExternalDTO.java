package ng.com.justjava.corebanking.service.dto;

import ng.com.justjava.corebanking.domain.enumeration.CorporateProfileRegistrationType;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

public class WalletExternalDTO {

    // Details for Corporate User Creation
    public String businessName;
    public String businessAddress;
    public CorporateProfileRegistrationType regType;
    public String category;
    public String rcNO;
    public String businessPhoneNo;
    public String tin;
    public boolean cacCertificate;
    public boolean cacCo7;
    public boolean cacCo2;
    public boolean utilityBill;
    public List<CorporateDocuments> corporateDocuments;
    @NotEmpty(message = "first name cannot be empty")
    private String firstName;
//    @NotEmpty(message = "last name cannot be empty")
    private String lastName;
    @Size(min = 11, max = 15, message = "Invalid phone number")
    private String phoneNumber;
    private String password;
    private String deviceNotificationToken;//
    private String pin;
    private String scheme;
    private LocalDate dateOfBirth;
    private String gender;
    private String photo;
    private String state;
    private String localGovt;
    private Double latitude;
    private Double longitude;
    private String address;
    //private Profile addressOwner;
    private String email;
    private double openingBalance; //
    private String accountName;
    private String accountNumber;//
    private String nin;//
    private String addressType;//
    private String city;//
    private Long walletAccountTypeId = 1L;//
    private Double walletLimit = 0.0;//
    private Long otp;//
    private int kyc;//
    private String bvn;//
    private boolean isAgent;//

    private String process_as = "NEW";

    public String getProcess_as() {
        return process_as;
    }

    public void setProcess_as(String process_as) {
        this.process_as = process_as;
    }

    public List<CorporateDocuments> getCorporateDocuments() {
        return corporateDocuments;
    }

    public void setCorporateDocuments(List<CorporateDocuments> corporateDocuments) {
        this.corporateDocuments = corporateDocuments;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getBusinessAddress() {
        return businessAddress;
    }

    public void setBusinessAddress(String businessAddress) {
        this.businessAddress = businessAddress;
    }

    public CorporateProfileRegistrationType getRegType() {
        return regType;
    }

    public void setRegType(CorporateProfileRegistrationType regType) {
        this.regType = regType;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getRcNO() {
        return rcNO;
    }

    public void setRcNO(String rcNO) {
        this.rcNO = rcNO;
    }

    public String getBusinessPhoneNo() {
        return businessPhoneNo;
    }

    public void setBusinessPhoneNo(String businessPhoneNo) {
        this.businessPhoneNo = businessPhoneNo;
    }

    public String getTin() {
        return tin;
    }

    public void setTin(String tin) {
        this.tin = tin;
    }

    public boolean isCacCertificate() {
        return cacCertificate;
    }

    public void setCacCertificate(boolean cacCertificate) {
        this.cacCertificate = cacCertificate;
    }

    public boolean isCacCo7() {
        return cacCo7;
    }

    public void setCacCo7(boolean cacCo7) {
        this.cacCo7 = cacCo7;
    }

    public boolean isCacCo2() {
        return cacCo2;
    }

    public void setCacCo2(boolean cacCo2) {
        this.cacCo2 = cacCo2;
    }

    public boolean isUtilityBill() {
        return utilityBill;
    }

    public void setUtilityBill(boolean utilityBill) {
        this.utilityBill = utilityBill;
    }


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

    public String getNin() {
        return nin;
    }

    public void setNin(String nin) {
        this.nin = nin;
    }

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Long getWalletAccountTypeId() {
        if (walletAccountTypeId == null) {
            return 1L;
        }
        return walletAccountTypeId;
    }

    public void setWalletAccountTypeId(Long walletAccountTypeId) {
        this.walletAccountTypeId = walletAccountTypeId;
    }

    public Long getOtp() {
        return otp;
    }

    public void setOtp(Long otp) {
        this.otp = otp;
    }

    public int getKyc() {
        return kyc;
    }

    public void setKyc(int kyc) {
        this.kyc = kyc;
    }

    public boolean isAgent() {
        return isAgent;
    }

    public void setAgent(boolean agent) {
        isAgent = agent;
    }

    @Override
    public String toString() {
        return "WalletExternalDTO{" +
            "businessName='" + businessName + '\'' +
            ", businessAddress='" + businessAddress + '\'' +
            ", regType=" + regType +
            ", category='" + category + '\'' +
            ", rcNO='" + rcNO + '\'' +
            ", businessPhoneNo='" + businessPhoneNo + '\'' +
            ", tin='" + tin + '\'' +
            ", cacCertificate=" + cacCertificate +
            ", cacCo7=" + cacCo7 +
            ", cacCo2=" + cacCo2 +
            ", utilityBill=" + utilityBill +
            ", corporateDocuments=" + corporateDocuments +
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
            ", localGovt='" + localGovt + '\'' +
            ", latitude=" + latitude +
            ", longitude=" + longitude +
            ", address='" + address + '\'' +
            ", email='" + email + '\'' +
            ", openingBalance=" + openingBalance +
            ", accountName='" + accountName + '\'' +
            ", accountNumber='" + accountNumber + '\'' +
            ", nin='" + nin + '\'' +
            ", addressType='" + addressType + '\'' +
            ", city='" + city + '\'' +
            ", walletAccountTypeId=" + walletAccountTypeId +
            ", walletLimit=" + walletLimit +
            ", otp=" + otp +
            ", kyc=" + kyc +
            ", bvn='" + bvn + '\'' +
            ", isAgent=" + isAgent +
            ", process_as='" + process_as + '\'' +
            '}';
    }

    public Double getWalletLimit() {
        return walletLimit;
    }

    public void setWalletLimit(Double walletLimit) {
        this.walletLimit = walletLimit;
    }
}
