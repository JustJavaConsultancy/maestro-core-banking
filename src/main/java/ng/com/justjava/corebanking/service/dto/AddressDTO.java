package ng.com.justjava.corebanking.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import ng.com.justjava.corebanking.domain.Address;
import ng.com.justjava.corebanking.domain.Profile;

import java.io.Serializable;

/**
 * A DTO for the {@link Address} entity.
 */
@ApiModel(description = "Address of the Actor within the Solution the Latitude and Longitude is critical")
public class AddressDTO implements Serializable {
    private String state;
    private String localGovt;
    private Double latitude;
    private Double longitude;
    private String address;
    private String addressType;
    private String city;

    @JsonIgnore
    private Profile addressOwner;

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

    public Profile getAddressOwner() {
        return addressOwner;
    }

    public void setAddressOwner(Profile addressOwner) {
        this.addressOwner = addressOwner;
    }

    @JsonIgnore
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


    @Override
    public String toString() {
        return "AddressDTO{" +
            "state='" + state + '\'' +
            ", localGovt='" + localGovt + '\'' +
            ", latitude=" + latitude +
            ", longitude=" + longitude +
            ", address='" + address + '\'' +
            ", addressType='" + addressType + '\'' +
            ", city='" + city + '\'' +
            '}';
    }
}
