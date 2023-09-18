package ng.com.systemspecs.apigateway.service.dto;

import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.*;

public class ExternallyCreatedCustomerDTO {

	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPin() {
		return pin;
	}
	public void setPin(String pin) {
		this.pin = pin;
	}
	public String getSecretKey() {
		return secretKey;
	}
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
	public String getSchemeId() {
		return schemeId;
	}
	public void setSchemeId(String schemeId) {
		this.schemeId = schemeId;
	}
	public String getExternalID() {
		return externalID;
	}
	public void setExternalID(String externalID) {
		this.externalID = externalID;
	}
	public String getAccountTypeId() {
		return accountTypeId;
	}
	public void setAccountTypeId(String accountTypeId) {
		this.accountTypeId = accountTypeId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getOtherName() {
		return otherName;
	}
	public void setOtherName(String otherName) {
		this.otherName = otherName;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public String getBvn() {
		return bvn;
	}
	public void setBvn(String bvn) {
		this.bvn = bvn;
	}
	public String getWalletName() {
		return walletName;
	}
	public void setWalletName(String walletName) {
		this.walletName = walletName;
	}
	public String getWalletAccountNumber() {
		return walletAccountNumber;
	}
	public void setWalletAccountNumber(String walletAccountNumber) {
		this.walletAccountNumber = walletAccountNumber;
	}
    public Double getOpeningBalance() {
	    if (openingBalance == null){
	        return 0.0;
        }
		return openingBalance ;
	}
	public void setOpeningBalance(Double openingBalance) {
        if (openingBalance == null){
            openingBalance = 0.0;
        }
		this.openingBalance = openingBalance;
	}

    private Double longitude;

	//@NotBlank(message = "Latitude is required")
	private Double latitude;

    @NotEmpty(message = "Phone is required")
	private String phone;

	@Email
	private String email;

	@NotEmpty(message = "Pin is required")
	private String pin;

	@NotEmpty(message = "SecretKey is required")
	private String secretKey;

	private String schemeId;

	private String externalID;

	private String accountTypeId;

	@NotEmpty(message = "FirstName is required")
	private String firstName;

	private String otherName;

	@NotEmpty(message = "Surname is required")
	private String surname;

	private String gender;

	@NotEmpty(message = "Date of birth is required")
	private String dateOfBirth;

	private String bvn;

	@NotEmpty(message = "Wallet Name is required")
	private String walletName;

	private String walletAccountNumber;

	private Double openingBalance;

    @Override
    public String toString() {
        return "ExternallyCreatedCustomerDTO{" +
            "longitude=" + longitude +
            ", latitude=" + latitude +
            ", phone='" + phone + '\'' +
            ", email='" + email + '\'' +
            ", pin='" + pin + '\'' +
            ", secretKey='" + secretKey + '\'' +
            ", schemeId=" + schemeId +
            ", externalID='" + externalID + '\'' +
            ", accountTypeId='" + accountTypeId + '\'' +
            ", firstName='" + firstName + '\'' +
            ", otherName='" + otherName + '\'' +
            ", surname='" + surname + '\'' +
            ", gender='" + gender + '\'' +
            ", dateOfBirth='" + dateOfBirth + '\'' +
            ", bvn='" + bvn + '\'' +
            ", walletName='" + walletName + '\'' +
            ", walletAccountNumber=" + walletAccountNumber +
            ", openingBalance=" + openingBalance +
            '}';
    }
}
