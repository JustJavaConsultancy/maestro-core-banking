package ng.com.justjava.corebanking.service.dto;

import javax.annotation.Generated;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import ng.com.justjava.corebanking.domain.enumeration.InsuranceOperation;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "secret_key",
    "operation",
    "policy_no",
    "certificate_no",
    "insured_name",
    "phone",
    "email",
    "address",
    "birth_date",
    "gender",
    "occupation",
    "sector",
    "state",
    "lga",
    "id_type",
    "id_number",
    "vehicle_type",
    "registration_no",
    "veh_make",
    "veh_model",
    "veh_year",
    "registration_start",
    "mileage",
    "veh_color",
    "chassis_no",
    "engine_no",
    "engine_capacity",
    "seat_capacity"
})
@Generated("jsonschema2pojo")
public class InsureVihicleRequestDTO {
	@JsonProperty("secret_key")
	@NotEmpty(message = "The secret key is compulsary")
	private String secretKey;

	@JsonProperty("operation")
	@NotEmpty(message = "The Operation field is compulsary")
    private InsuranceOperation operation;

	@JsonProperty("policy_no")
    private String policyNo;

	@JsonProperty("certificate_no")
    private String certificateNo;

	@JsonProperty("insured_name")
	@NotEmpty(message = "The insured name is compulsary")
    private String insuredName;

	@JsonProperty("phone")
    @NotEmpty(message = "The phone number field is compulsary")
    private String phone;

	@JsonProperty("email")
	@NotEmpty(message = "The email filed is compulsary")
    private String email;

	@JsonProperty("address")
    @NotEmpty(message = "The address field is compulsary")
    private String address;

	@JsonProperty("birth_date")
    @NotEmpty(message = "The birth date field is compusary")
    private String birthDate;

	@JsonProperty("gender")
    private String gender;

	@JsonProperty("occupation")
    @NotEmpty(message = "The occupation field is compusary")
    private String occupation;

	@JsonProperty("sector")
    @NotEmpty(message = "The sector field is compusary")
    private String sector;

	@JsonProperty("state")
    @NotEmpty(message = "The state field is compusary")
    private String state;

	@JsonProperty("lga")
    @NotEmpty(message = "The lga field is compusary")
    private String lga;

	@JsonProperty("id_type")
    @NotEmpty(message = "The id_type field is compusary")
    private String idType;

	@JsonProperty("id_number")
    @NotEmpty(message = "The id_number field is compusary")
    private String idNumber;

	@JsonProperty("vehicle_type")
    @NotEmpty(message = "The vehicle_type field is compusary")
    private String vehicleType;

	@JsonProperty("registration_no")
    @NotEmpty(message = "The registration_no field is compusary")
    private String registrationNo;

	@JsonProperty("veh_make")
    @NotEmpty(message = "The veh_make field is compusary")
	private String vehMake;

	@JsonProperty("veh_model")
    @NotEmpty(message = "The veh_model field is compusary")
    private String vehModel;

	@JsonProperty("veh_year")
    @NotEmpty(message = "The veh_year field is compusary")
    private String vehYear;

	@JsonProperty("registration_start")
    @NotEmpty(message = "The registration_start field is compusary")
    private String registrationStart;

	@JsonProperty("mileage")
    @NotEmpty(message = "The mileage field is compusary")
    private String mileage;

	@JsonProperty("veh_color")
    @NotEmpty(message = "The veh_color field is compusary")
    private String vehColor;

	@JsonProperty("chassis_no")
    @NotEmpty(message = "The chassis_no field is compusary")
    private String chassisNo;

	@JsonProperty("engine_no")
    @NotEmpty(message = "The engine_no field is compusary")
    private String engineNo;

	@JsonProperty("engine_capacity")
    @NotEmpty(message = "The engine_capacity field is compusary")
    private String engineCapacity;

	@JsonProperty("seat_capacity")
    @NotEmpty(message = "The seat_capacity field is compusary")
    private String seatCapacity;

	@JsonProperty("secret_key")
	public String getSecretKey() {
		return secretKey;
	}

	@JsonProperty("secret_key")
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	@JsonProperty("operation")
	public InsuranceOperation getOperation() {
		return operation;
	}

	@JsonProperty("operation")
	public void setOperation(InsuranceOperation operation) {
		this.operation = operation;
	}

	@JsonProperty("policy_no")
	public String getPolicyNo() {
		return policyNo;
	}

	@JsonProperty("policy_no")
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	@JsonProperty("certificate_no")
	public String getCertificateNo() {
		return certificateNo;
	}

	@JsonProperty("certificate_no")
	public void setCertificateNo(String certificateNo) {
		this.certificateNo = certificateNo;
	}

	@JsonProperty("insured_name")
	public String getInsuredName() {
		return insuredName;
	}

	@JsonProperty("insured_name")
	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}

	@JsonProperty("phone")
	public String getPhone() {
		return phone;
	}

	@JsonProperty("phone")
	public void setPhone(String phone) {
		this.phone = phone;
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

	@JsonProperty("birth_date")
	public String getBirthDate() {
		return birthDate;
	}

	@JsonProperty("birth_date")
	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	@JsonProperty("gender")
	public String getGender() {
		return gender;
	}

	@JsonProperty("gender")
	public void setGender(String gender) {
		this.gender = gender;
	}

	@JsonProperty("occupation")
	public String getOccupation() {
		return occupation;
	}

	@JsonProperty("occupation")
	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	@JsonProperty("sector")
	public String getSector() {
		return sector;
	}

	@JsonProperty("sector")
	public void setSector(String sector) {
		this.sector = sector;
	}

	@JsonProperty("state")
	public String getState() {
		return state;
	}

	@JsonProperty("state")
	public void setState(String state) {
		this.state = state;
	}

	@JsonProperty("lga")
	public String getLga() {
		return lga;
	}

	@JsonProperty("lga")
	public void setLga(String lga) {
		this.lga = lga;
	}

	@JsonProperty("id_type")
	public String getIdType() {
		return idType;
	}

	@JsonProperty("id_type")
	public void setIdType(String idType) {
		this.idType = idType;
	}

	@JsonProperty("id_number")
	public String getIdNumber() {
		return idNumber;
	}

	@JsonProperty("id_number")
	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	@JsonProperty("vehicle_type")
	public String getVehicleType() {
		return vehicleType;
	}

	@JsonProperty("vehicle_type")
	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}

	@JsonProperty("registration_no")
	public String getRegistrationNo() {
		return registrationNo;
	}

	@JsonProperty("registration_no")
	public void setRegistrationNo(String registrationNo) {
		this.registrationNo = registrationNo;
	}

	@JsonProperty("veh_make")
	public String getVehMake() {
		return vehMake;
	}

	@JsonProperty("veh_make")
	public void setVehMake(String vehMake) {
		this.vehMake = vehMake;
	}

	@JsonProperty("veh_model")
	public String getVehModel() {
		return vehModel;
	}

	@JsonProperty("veh_model")
	public void setVehModel(String vehModel) {
		this.vehModel = vehModel;
	}

	@JsonProperty("veh_year")
	public String getVehYear() {
		return vehYear;
	}

	@JsonProperty("veh_year")
	public void setVehYear(String vehYear) {
		this.vehYear = vehYear;
	}

	@JsonProperty("registration_start")
	public String getRegistrationStart() {
		return registrationStart;
	}

	@JsonProperty("registration_start")
	public void setRegistrationStart(String registrationStart) {
		this.registrationStart = registrationStart;
	}

	@JsonProperty("mileage")
	public String getMileage() {
		return mileage;
	}

	@JsonProperty("mileage")
	public void setMileage(String mileage) {
		this.mileage = mileage;
	}

	@JsonProperty("veh_color")
	public String getVehColor() {
		return vehColor;
	}

	@JsonProperty("veh_color")
	public void setVehColor(String vehColor) {
		this.vehColor = vehColor;
	}

	@JsonProperty("chassis_no")
	public String getChassisNo() {
		return chassisNo;
	}

	@JsonProperty("chassis_no")
	public void setChassisNo(String chassisNo) {
		this.chassisNo = chassisNo;
	}

	@JsonProperty("engine_no")
	public String getEngineNo() {
		return engineNo;
	}

	@JsonProperty("engine_no")
	public void setEngineNo(String engineNo) {
		this.engineNo = engineNo;
	}

	@JsonProperty("engine_capacity")
	public String getEngineCapacity() {
		return engineCapacity;
	}

	@JsonProperty("engine_capacity")
	public void setEngineCapacity(String engineCapacity) {
		this.engineCapacity = engineCapacity;
	}

	@JsonProperty("seat_capacity")
	public String getSeatCapacity() {
		return seatCapacity;
	}

	@JsonProperty("seat_capacity")
	public void setSeatCapacity(String seatCapacity) {
		this.seatCapacity = seatCapacity;
	}
}
