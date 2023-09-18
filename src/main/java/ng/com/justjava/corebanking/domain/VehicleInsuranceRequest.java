package ng.com.justjava.corebanking.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ng.com.justjava.corebanking.domain.enumeration.InsuranceOperation;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A VehicleInsuranceRequest.
 */
@Entity
@Table(name = "vehicle_insurance_request")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class VehicleInsuranceRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "operation", nullable = false)
    private InsuranceOperation operation;

    @Column(name = "policy_no")
    private String policyNo;

    @Column(name = "certificate_no")
    private String certificateNo;

    @NotNull
    @Column(name = "occupation", nullable = false)
    private String occupation;

    @NotNull
    @Column(name = "sector", nullable = false)
    private String sector;

    @NotNull
    @Column(name = "id_type", nullable = false)
    private String idType;

    @NotNull
    @Column(name = "id_number", nullable = false)
    private String idNumber;

    @NotNull
    @Column(name = "vehicle_type", nullable = false)
    private String vehicleType;

    @NotNull
    @Column(name = "registration_no", nullable = false)
    private String registrationNo;

    @NotNull
    @Column(name = "veh_make", nullable = false)
    private String vehMake;

    @NotNull
    @Column(name = "veh_model", nullable = false)
    private String vehModel;

    @NotNull
    @Column(name = "veh_year", nullable = false)
    private Integer vehYear;

    @NotNull
    @Column(name = "registration_start", nullable = false)
    private String registrationStart;

    @NotNull
    @Column(name = "expiry_date", nullable = false)
    private String expiryDate;

    @NotNull
    @Column(name = "mileage", nullable = false)
    private String mileage;

    @NotNull
    @Column(name = "veh_color", nullable = false)
    private String vehColor;

    @NotNull
    @Column(name = "chassis_no", nullable = false)
    private String chassisNo;

    @NotNull
    @Column(name = "engine_no", nullable = false)
    private String engineNo;

    @NotNull
    @Column(name = "engine_capacity", nullable = false)
    private String engineCapacity;

    @NotNull
    @Column(name = "seat_capacity", nullable = false)
    private String seatCapacity;

    @Column(name = "balance")
    private Double balance;

    @Column(name = "certificate_url")
    private String certificateURL;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "vehicleInsuranceRequests", allowSetters = true)
    private Profile profile;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "vehicleInsuranceRequests", allowSetters = true)
    private InsuranceType insuranceType;

    // jhipster-needle-entity-add-field - JHipster will add fields here



    public Long getId() {
        return id;
    }

    public String getCertificateURL() {
		return certificateURL;
	}

	public void setCertificateURL(String certificateURL) {
		this.certificateURL = certificateURL;
	}

	public void setId(Long id) {
        this.id = id;
    }

    public InsuranceOperation getOperation() {
        return operation;
    }

    public VehicleInsuranceRequest operation(InsuranceOperation operation) {
        this.operation = operation;
        return this;
    }

    public void setOperation(InsuranceOperation operation) {
        this.operation = operation;
    }

    public String getPolicyNo() {
        return policyNo;
    }

    public VehicleInsuranceRequest policyNo(String policyNo) {
        this.policyNo = policyNo;
        return this;
    }

    public void setPolicyNo(String policyNo) {
        this.policyNo = policyNo;
    }

    public String getCertificateNo() {
        return certificateNo;
    }

    public VehicleInsuranceRequest certificateNo(String certificateNo) {
        this.certificateNo = certificateNo;
        return this;
    }

    public void setCertificateNo(String certificateNo) {
        this.certificateNo = certificateNo;
    }

    public String getOccupation() {
        return occupation;
    }

    public VehicleInsuranceRequest occupation(String occupation) {
        this.occupation = occupation;
        return this;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getSector() {
        return sector;
    }

    public VehicleInsuranceRequest sector(String sector) {
        this.sector = sector;
        return this;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getIdType() {
        return idType;
    }

    public VehicleInsuranceRequest idType(String idType) {
        this.idType = idType;
        return this;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public VehicleInsuranceRequest idNumber(String idNumber) {
        this.idNumber = idNumber;
        return this;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public VehicleInsuranceRequest vehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
        return this;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getRegistrationNo() {
        return registrationNo;
    }

    public VehicleInsuranceRequest registrationNo(String registrationNo) {
        this.registrationNo = registrationNo;
        return this;
    }

    public void setRegistrationNo(String registrationNo) {
        this.registrationNo = registrationNo;
    }

    public String getVehMake() {
        return vehMake;
    }

    public VehicleInsuranceRequest vehMake(String vehMake) {
        this.vehMake = vehMake;
        return this;
    }

    public void setVehMake(String vehMake) {
        this.vehMake = vehMake;
    }

    public String getVehModel() {
        return vehModel;
    }

    public VehicleInsuranceRequest vehModel(String vehModel) {
        this.vehModel = vehModel;
        return this;
    }

    public void setVehModel(String vehModel) {
        this.vehModel = vehModel;
    }

    public Integer getVehYear() {
        return vehYear;
    }

    public VehicleInsuranceRequest vehYear(Integer vehYear) {
        this.vehYear = vehYear;
        return this;
    }

    public void setVehYear(Integer vehYear) {
        this.vehYear = vehYear;
    }

    public String getRegistrationStart() {
        return registrationStart;
    }

    public VehicleInsuranceRequest registrationStart(String registrationStart) {
        this.registrationStart = registrationStart;
        return this;
    }

    public void setRegistrationStart(String registrationStart) {
        this.registrationStart = registrationStart;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public VehicleInsuranceRequest expiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
        return this;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getMileage() {
        return mileage;
    }

    public VehicleInsuranceRequest mileage(String mileage) {
        this.mileage = mileage;
        return this;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }

    public String getVehColor() {
        return vehColor;
    }

    public VehicleInsuranceRequest vehColor(String vehColor) {
        this.vehColor = vehColor;
        return this;
    }

    public void setVehColor(String vehColor) {
        this.vehColor = vehColor;
    }

    public String getChassisNo() {
        return chassisNo;
    }

    public VehicleInsuranceRequest chassisNo(String chassisNo) {
        this.chassisNo = chassisNo;
        return this;
    }

    public void setChassisNo(String chassisNo) {
        this.chassisNo = chassisNo;
    }

    public String getEngineNo() {
        return engineNo;
    }

    public VehicleInsuranceRequest engineNo(String engineNo) {
        this.engineNo = engineNo;
        return this;
    }

    public void setEngineNo(String engineNo) {
        this.engineNo = engineNo;
    }

    public String getEngineCapacity() {
        return engineCapacity;
    }

    public VehicleInsuranceRequest engineCapacity(String engineCapacity) {
        this.engineCapacity = engineCapacity;
        return this;
    }

    public void setEngineCapacity(String engineCapacity) {
        this.engineCapacity = engineCapacity;
    }

    public String getSeatCapacity() {
        return seatCapacity;
    }

    public VehicleInsuranceRequest seatCapacity(String seatCapacity) {
        this.seatCapacity = seatCapacity;
        return this;
    }

    public void setSeatCapacity(String seatCapacity) {
        this.seatCapacity = seatCapacity;
    }

    public Double getBalance() {
        return balance;
    }

    public VehicleInsuranceRequest balance(Double balance) {
        this.balance = balance;
        return this;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Profile getProfile() {
        return profile;
    }

    public VehicleInsuranceRequest profile(Profile profile) {
        this.profile = profile;
        return this;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public InsuranceType getInsuranceType() {
        return insuranceType;
    }

    public VehicleInsuranceRequest insuranceType(InsuranceType insuranceType) {
        this.insuranceType = insuranceType;
        return this;
    }

    public void setInsuranceType(InsuranceType insuranceType) {
        this.insuranceType = insuranceType;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VehicleInsuranceRequest)) {
            return false;
        }
        return id != null && id.equals(((VehicleInsuranceRequest) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

	@Override
	public String toString() {
		return "VehicleInsuranceRequest [id=" + id + ", operation=" + operation + ", policyNo=" + policyNo
				+ ", certificateNo=" + certificateNo + ", occupation=" + occupation + ", sector=" + sector + ", idType="
				+ idType + ", idNumber=" + idNumber + ", vehicleType=" + vehicleType + ", registrationNo="
				+ registrationNo + ", vehMake=" + vehMake + ", vehModel=" + vehModel + ", vehYear=" + vehYear
				+ ", registrationStart=" + registrationStart + ", expiryDate=" + expiryDate + ", mileage=" + mileage
				+ ", vehColor=" + vehColor + ", chassisNo=" + chassisNo + ", engineNo=" + engineNo + ", engineCapacity="
				+ engineCapacity + ", seatCapacity=" + seatCapacity + ", balance=" + balance + ", certificateURL="
				+ certificateURL + ", profile=" + profile + ", insuranceType=" + insuranceType + "]";
	}

    // prettier-ignore

}
