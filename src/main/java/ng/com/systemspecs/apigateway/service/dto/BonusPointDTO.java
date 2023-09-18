package ng.com.systemspecs.apigateway.service.dto;

import ng.com.systemspecs.apigateway.domain.Journal;

import java.io.Serializable;

/**
 * A DTO for the {@link ng.com.systemspecs.apigateway.domain.BonusPoint} entity.
 */
public class BonusPointDTO implements Serializable {

    private Long id;

    private Double amount;

    private String remark;

    private Journal journal;

    private String profileId;

    private String profilePhoneNumber;

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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getProfilePhoneNumber() {
        return profilePhoneNumber;
    }

    public void setProfilePhoneNumber(String profilePhoneNumber) {
        this.profilePhoneNumber = profilePhoneNumber;
    }

    public Journal getJournal() {
        return journal;
    }

    public void setJournal(Journal journal) {
        this.journal = journal;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BonusPointDTO)) {
            return false;
        }

        return id != null && id.equals(((BonusPointDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore

    @Override
    public String toString() {
        return "BonusPointDTO{" +
            "id=" + id +
            ", amount=" + amount +
            ", remark='" + remark + '\'' +
            ", journal=" + journal +
            ", profileId=" + profileId +
            ", profilePhoneNumber='" + profilePhoneNumber + '\'' +
            '}';
    }
}
