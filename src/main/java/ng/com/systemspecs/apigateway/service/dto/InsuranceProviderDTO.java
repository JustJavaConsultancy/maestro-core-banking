package ng.com.systemspecs.apigateway.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * A DTO for the {@link ng.com.systemspecs.apigateway.domain.InsuranceProvider} entity.
 */
public class InsuranceProviderDTO implements Serializable {

    private Long id;

    @NotNull
    private Double rate;

    @NotNull
    private String preferred_tenure;


    private Long profileId;

    private String profilePhoneNumber;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public String getPreferred_tenure() {
        return preferred_tenure;
    }

    public void setPreferred_tenure(String preferred_tenure) {
        this.preferred_tenure = preferred_tenure;
    }

    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }

    public String getProfilePhoneNumber() {
        return profilePhoneNumber;
    }

    public void setProfilePhoneNumber(String profilePhoneNumber) {
        this.profilePhoneNumber = profilePhoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InsuranceProviderDTO)) {
            return false;
        }

        return id != null && id.equals(((InsuranceProviderDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InsuranceProviderDTO{" +
            "id=" + getId() +
            ", rate=" + getRate() +
            ", preferred_tenure='" + getPreferred_tenure() + "'" +
            ", profileId=" + getProfileId() +
            ", profilePhoneNumber='" + getProfilePhoneNumber() + "'" +
            "}";
    }
}
