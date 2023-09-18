package ng.com.systemspecs.apigateway.service.dto;

import ng.com.systemspecs.apigateway.domain.enumeration.LoanType;
import ng.com.systemspecs.apigateway.domain.enumeration.Tenure;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * A DTO for the {@link ng.com.systemspecs.apigateway.domain.Lender} entity.
 */
public class LenderDTO implements Serializable {

    private Long id;

    @NotNull
    private Double rate;

    @NotNull
    private Tenure preferredTenure;

    private String name;

    private Long profileId;

    private String profilePhoneNumber;

    private LoanType loanType;

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

    public Tenure getPreferredTenure() {
        return preferredTenure;
    }

    public void setPreferredTenure(Tenure preferredTenure) {
        this.preferredTenure = preferredTenure;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProfilePhoneNumber(String profilePhoneNumber) {
        this.profilePhoneNumber = profilePhoneNumber;
    }

    public LoanType getLenderType() {
        return loanType;
    }

    public void setLenderType(LoanType loanType) {
        this.loanType = loanType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LenderDTO)) {
            return false;
        }

        return id != null && id.equals(((LenderDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LenderDTO{" +
            "id=" + getId() +
            ", rate=" + getRate() +
            ", preferedTenure='" + getPreferredTenure() + "'" +
            ", profileId=" + getProfileId() +
            ", profilePhoneNumber='" + getProfilePhoneNumber() + "'" +
            ", loanType='" + getLenderType() + "'" +
            "}";
    }
}
