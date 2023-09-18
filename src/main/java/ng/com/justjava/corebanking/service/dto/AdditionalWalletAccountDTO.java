package ng.com.justjava.corebanking.service.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;

public class AdditionalWalletAccountDTO implements Serializable {

    @NotEmpty
    private String requestId;

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String surname;
    private String otherName;

    @Email(message = "Email has invalid format", regexp = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$")
    private String email;

	@Size(min = 11, max = 15, message = "phone number must be greater than 10 and less than 15 characters")
	private String phone;
	private String dateOfBirth;

	private String customerId;

    @NotEmpty
	private String accountTypeId;

    @NotEmpty
	private String gender;

    @NotEmpty
	private String schemeCode;

    @Size(min = 4, max = 4, message = "transaction pin must be 4 digits")
	private String transactionPin;

    @NotEmpty
	private String secretKey;
	private boolean isRestrictedWallet;


    public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public String getOtherName() {
		return otherName;
	}
	public void setOtherName(String otherName) {
		this.otherName = otherName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getAccountTypeId() {
		return accountTypeId;
	}
	public void setAccountTypeId(String accountTypeId) {
		this.accountTypeId = accountTypeId;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getSchemeCode() {
		return schemeCode;
	}
	public void setSchemeCode(String schemeCode) {
		this.schemeCode = schemeCode;
	}
	public String getTransactionPin() {
		return transactionPin;
	}
	public void setTransactionPin(String transactionPin) {
		this.transactionPin = transactionPin;
	}
	public String getSecretKey() {
		return secretKey;
	}
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
	public boolean isRestrictedWallet() {
		return isRestrictedWallet;
	}
	public void setRestrictedWallet(boolean isRestrictedWallet) {
		this.isRestrictedWallet = isRestrictedWallet;
	}


}
