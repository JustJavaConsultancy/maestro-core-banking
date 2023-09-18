package ng.com.justjava.corebanking.service.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;


public class RegistrationLastPageDTO {

    @Past
    @NotNull(message = "Date of Birth is required field")
    private LocalDate dateOfBirth;

    @NotEmpty(message = "Gender is required")
    private String gender;

    private String photo;
    private String scheme;

    @NotEmpty(message = "State is required field")
    private String state;

    @NotEmpty(message = "local government is required field")
    private String localGovt;
    private Double latitude;
    private Double longitude;

    @NotEmpty
    private String address;
    //private Profile addressOwner;

    @Email(message = "Enter a valid email address")
    private String email;

    private String bvn;
    @NotEmpty(message = "Security question is required field")
    private String secretQuestion;

    @NotEmpty(message = "Security Answer is required field")
    private String secretAnswer;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
//	public Profile getAddressOwner() {
//		return addressOwner;
//	}
//	public void setAddressOwner(Profile addressOwner) {
//		this.addressOwner = addressOwner;
//	}


    public String getSecretQuestion() {
        return secretQuestion;
    }

    public void setSecretQuestion(String secretQuestion) {
        this.secretQuestion = secretQuestion;
    }

    public String getSecretAnswer() {
        return secretAnswer;
    }

    public void setSecretAnswer(String secretAnswer) {
        this.secretAnswer = secretAnswer;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    @Override
    public String toString() {
        return "RegistrationLastPageDTO [dateOfBirth=" + dateOfBirth + ", gender=" + gender + ", photo=" + photo
            + ", scheme=" + scheme + ", state=" + state + ", localGovt=" + localGovt + ", latitude=" + latitude + ", longitude="
            + longitude + ", address=" + address + ", email=" + email + "]";
    }

    public String getBvn() {
        return bvn;
    }

    public void setBvn(String bvn) {
        this.bvn = bvn;
	}

}
