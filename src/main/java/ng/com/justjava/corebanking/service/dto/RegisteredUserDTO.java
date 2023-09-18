package ng.com.justjava.corebanking.service.dto;

import ng.com.justjava.corebanking.util.ValidPassword;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class RegisteredUserDTO {

    @NotEmpty(message = "firstname cannot be empty")
    private String firstName;

    @NotEmpty(message = "firstname cannot be empty")
    private String lastName;

    @Size(min = 11, max = 15, message = "phone number must be greater than 10 and less than 15 characters")
    private String phoneNumber;

    @ValidPassword(message = "Invalid password, please input a strong password")
    private String password;

    @Email
    private String email;

    private String deviceNotificationToken;

    @Size(max = 11)
    private String nin;

    @Size(max = 11)
    private String bvn;

    public String getDeviceNotificationToken() {
        return deviceNotificationToken;
    }

    public void setDeviceNotificationToken(String deviceNotificationToken) {
        this.deviceNotificationToken = deviceNotificationToken;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNin() {
        return nin;
    }

    public void setNin(String nin) {
        this.nin = nin;
    }

    public String getBvn() {
        return bvn;
    }

    public void setBvn(String bvn) {
        this.bvn = bvn;
    }

    @Override
    public String toString() {
        return "RegisteredUserDTO{" +
            "firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", phoneNumber='" + phoneNumber + '\'' +
            ", email='" + email + '\'' +
            ", deviceNotificationToken='" + deviceNotificationToken + '\'' +
            ", nin='" + nin + '\'' +
            '}';
    }
}
