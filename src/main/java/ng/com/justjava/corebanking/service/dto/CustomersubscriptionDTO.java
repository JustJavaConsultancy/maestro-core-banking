package ng.com.justjava.corebanking.service.dto;

import java.io.Serializable;

import ng.com.justjava.corebanking.domain.Customersubscription;
import ng.com.justjava.corebanking.domain.enumeration.Frequency;

/**
 * A DTO for the {@link Customersubscription} entity.
 */
public class CustomersubscriptionDTO implements Serializable {

    private Long id;

    private String uniqueID;

    private Frequency frequency;


    private Long phoneNumberId;

    private Long billerId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    public Frequency getFrequency() {
        return frequency;
    }

    public void setFrequency(Frequency frequency) {
        this.frequency = frequency;
    }

    public Long getPhoneNumberId() {
        return phoneNumberId;
    }

    public void setPhoneNumberId(Long profileId) {
        this.phoneNumberId = profileId;
    }

    public Long getBillerId() {
        return billerId;
    }

    public void setBillerId(Long billerId) {
        this.billerId = billerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CustomersubscriptionDTO)) {
            return false;
        }

        return id != null && id.equals(((CustomersubscriptionDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CustomersubscriptionDTO{" +
            "id=" + getId() +
            ", uniqueID='" + getUniqueID() + "'" +
            ", frequency='" + getFrequency() + "'" +
            ", phoneNumberId=" + getPhoneNumberId() +
            ", billerId=" + getBillerId() +
            "}";
    }
}
