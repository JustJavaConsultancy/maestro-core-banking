package ng.com.justjava.corebanking.service.dto;

import ng.com.justjava.corebanking.domain.VehicleInsuranceRequest;
import ng.com.justjava.corebanking.domain.enumeration.InsuranceOperation;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * A DTO for the {@link VehicleInsuranceRequest} entity.
 */
public class VehicleInsuranceRequestDTO implements Serializable {

    private Long id;

    @NotNull(message = "Operation field is compulsory")
    private InsuranceOperation operation;

    private String policyNo;

    private String certificateNo;

    private String certificateURL;

    private Long lenderId;

    public Long getLenderId() {
		return lenderId;
	}

	public void setLenderId(Long lenderId) {
		this.lenderId = lenderId;
    }

    public String getCertificateURL() {
        return certificateURL;
    }

    public void setCertificateURL(String certificateURL) {
        this.certificateURL = certificateURL;
    }

    @NotNull(message = "occupation field is compulsory")
    private String occupation;

    @NotNull(message = "sector field is compulsory")
    private String sector;

    @NotNull(message = "idType field is compulsory")
    private String idType;

    @NotNull(message = "idNumber field is compulsory")
    private String idNumber;

    @NotNull(message = "vehicleType field is compulsory")
    private String vehicleType;

    @NotNull(message = "registrationNo field is compulsory")
    private String registrationNo;

    @NotNull(message = "vehicle Make field is compulsory")
    private String vehMake;

    @NotNull(message = "vehicle Model field is compulsory")
    private String vehModel;

    @NotNull(message = "vehYear field is compulsory")
    private Integer vehYear;

    @NotNull(message = "registrationStart field is compulsory")
    private String registrationStart;

    private String expiryDate;

    @NotNull(message = "mileage field is compulsory")
    private String mileage;

    @NotNull(message = "vehicle Color field is compulsory")
    private String vehColor;

    @NotNull(message = "chassisNo field is compulsory")
    private String chassisNo;

    @NotNull(message = "engineNo field is compulsory")
    private String engineNo;

    @NotNull(message = "engineCapacity field is compulsory")
    private String engineCapacity;

    @NotNull(message = "seatCapacity field is compulsory")
    private String seatCapacity;


    private Double balance;

    @NotNull(message = "address field is compulsory")
    private String address;

    @NotNull(message = "state field is compulsory")
    private String state;

    @NotNull(message = "lga field is compulsory")
    private String lga;

    @NotNull(message = "You must enter your last name")
    private String lastName;

    @NotNull(message = "You must enter your first name")
    private String firstName;

    @NotNull(message = "You must enter a valid phone number")
    private String phoneNumber;

    @NotNull(message = "You must enter a valid email address")
    private String email;

    @NotNull(message = "You must select a gender")
    private String gender;

    @NotNull(message = "You must enter your date of birth")
    private String dateOfBirth;

    @NotNull(message = "You must enter your Pin")
    private String pin;

    public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	@NotNull(message = "You must select a wallet to debit")
    private String accountNumber;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getLga() {
		return lga;
	}

	public void setLga(String lga) {
		this.lga = lga;
	}

	private Long profileId;

    private Long insuranceTypeId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public InsuranceOperation getOperation() {
        return operation;
    }

    public void setOperation(InsuranceOperation operation) {
        this.operation = operation;
    }

    public String getPolicyNo() {
        return policyNo;
    }

    public void setPolicyNo(String policyNo) {
        this.policyNo = policyNo;
    }

    public String getCertificateNo() {
        return certificateNo;
    }

    public void setCertificateNo(String certificateNo) {
        this.certificateNo = certificateNo;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getRegistrationNo() {
        return registrationNo;
    }

    public void setRegistrationNo(String registrationNo) {
        this.registrationNo = registrationNo;
    }

    public String getVehMake() {
        return vehMake;
    }

    public void setVehMake(String vehMake) {
        this.vehMake = vehMake;
    }

    public String getVehModel() {
        return vehModel;
    }

    public void setVehModel(String vehModel) {
        this.vehModel = vehModel;
    }

    public Integer getVehYear() {
        return vehYear;
    }

    public void setVehYear(Integer vehYear) {
        this.vehYear = vehYear;
    }

    public String getRegistrationStart() {
        return registrationStart;
    }

    public void setRegistrationStart(String registrationStart) {
        this.registrationStart = registrationStart;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getMileage() {
        return mileage;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }

    public String getVehColor() {
        return vehColor;
    }

    public void setVehColor(String vehColor) {
        this.vehColor = vehColor;
    }

    public String getChassisNo() {
        return chassisNo;
    }

    public void setChassisNo(String chassisNo) {
        this.chassisNo = chassisNo;
    }

    public String getEngineNo() {
        return engineNo;
    }

    public void setEngineNo(String engineNo) {
        this.engineNo = engineNo;
    }

    public String getEngineCapacity() {
        return engineCapacity;
    }

    public void setEngineCapacity(String engineCapacity) {
        this.engineCapacity = engineCapacity;
    }

    public String getSeatCapacity() {
        return seatCapacity;
    }

    public void setSeatCapacity(String seatCapacity) {
        this.seatCapacity = seatCapacity;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }

    public Long getInsuranceTypeId() {
        return insuranceTypeId;
    }

    public void setInsuranceTypeId(Long insuranceTypeId) {
        this.insuranceTypeId = insuranceTypeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VehicleInsuranceRequestDTO)) {
            return false;
        }

        return id != null && id.equals(((VehicleInsuranceRequestDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

	@Override
	public String toString() {
		return "VehicleInsuranceRequestDTO [id=" + id + ", operation=" + operation + ", policyNo=" + policyNo
				+ ", certificateNo=" + certificateNo + ", certificateURL=" + certificateURL + ", lenderId=" + lenderId
				+ ", occupation=" + occupation + ", sector=" + sector + ", idType=" + idType + ", idNumber=" + idNumber
				+ ", vehicleType=" + vehicleType + ", registrationNo=" + registrationNo + ", vehMake=" + vehMake
				+ ", vehModel=" + vehModel + ", vehYear=" + vehYear + ", registrationStart=" + registrationStart
				+ ", expiryDate=" + expiryDate + ", mileage=" + mileage + ", vehColor=" + vehColor + ", chassisNo="
				+ chassisNo + ", engineNo=" + engineNo + ", engineCapacity=" + engineCapacity + ", seatCapacity="
				+ seatCapacity + ", balance=" + balance + ", address=" + address + ", state=" + state + ", lga=" + lga
				+ ", lastName=" + lastName + ", firstName=" + firstName + ", phoneNumber=" + phoneNumber + ", email="
				+ email + ", gender=" + gender + ", dateOfBirth=" + dateOfBirth + ", accountNumber=" + accountNumber
				+ ", profileId=" + profileId + ", insuranceTypeId=" + insuranceTypeId + "]";
	}







    // prettier-ignore

}
