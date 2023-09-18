package ng.com.systemspecs.apigateway.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;

import java.io.Serializable;

/**
 * A DTO for the {@link ng.com.systemspecs.apigateway.domain.Biller} entity.
 */
@ApiModel(description = "This setup different Billers in the system a Customer can be in")
public class BillerDTO implements Serializable {


    @JsonIgnore
    private Long id;

    private String billerID;

    private String biller;

    
    @JsonIgnore
    private String address;

    @JsonIgnore
    private String phoneNumber;

    private boolean isPopular;

    @JsonIgnore
    private Long billerCategoryId;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBillerID() {
        return billerID;
    }

    public void setBillerID(String billerID) {
        this.billerID = billerID;
    }

    public String getBiller() {
        return biller;
    }

    public void setBiller(String biller) {
        this.biller = biller;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Long getBillerCategoryId() {
        return billerCategoryId;
    }

    public void setBillerCategoryId(Long billerCategoryId) {
        this.billerCategoryId = billerCategoryId;
    }


    public boolean isPopular() {
		return isPopular;
	}

	public void setPopular(boolean isPopular) {
		this.isPopular = isPopular;
	}


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BillerDTO)) {
            return false;
        }

        return id != null && id.equals(((BillerDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BillerDTO{" +
            "id=" + getId() +
            ", billerID=" + getBillerID() +  
            ", biller='" + getBiller() + "'" +
            ", address='" + getAddress() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", billerCategoryId=" + getBillerCategoryId() +  
            "}";  
    }
}
