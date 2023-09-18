package ng.com.systemspecs.apigateway.service.dto;

import ng.com.systemspecs.apigateway.domain.Lender;
import ng.com.systemspecs.apigateway.domain.enumeration.LoanStatus;

import java.io.Serializable;

/**
 * A DTO for the {@link ng.com.systemspecs.apigateway.domain.Loan} entity.
 */
public class LoansCreatedDTO implements Serializable {

    private Long id;

    private Double amount;

    private String tenure;

    private LoanStatus status;

    private Long profileId;

    private Long lenderId;
    
    private Lender lender;
    

    public Lender getLender() {
		return lender;
	}

	public void setLender(Lender lender) {
		this.lender = lender;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getTenure() {
        return tenure;
    }

    public void setTenure(String tenure) {
        this.tenure = tenure;
    }

    public LoanStatus getStatus() {
        return status;
    }

    public void setStatus(LoanStatus status) {
        this.status = status;
    }

    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }

    public Long getLenderId() {
        return lenderId;
    }

    public void setLenderId(Long lenderId) {
        this.lenderId = lenderId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LoanDTO)) {
            return false;
        }

        return id != null && id.equals(((LoansCreatedDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LoanDTO{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            ", tenure='" + getTenure() + "'" +
            ", status='" + getStatus() + "'" +
            ", profileId=" + getProfileId() +
            ", lenderId=" + getLenderId() +
            "}";
    }
}
