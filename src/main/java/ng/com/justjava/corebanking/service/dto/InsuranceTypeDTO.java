package ng.com.justjava.corebanking.service.dto;

import ng.com.justjava.corebanking.domain.InsuranceType;

import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link InsuranceType} entity.
 */
public class InsuranceTypeDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private Double charge;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getCharge() {
        return charge;
    }

    public void setCharge(Double charge) {
        this.charge = charge;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InsuranceTypeDTO)) {
            return false;
        }

        return id != null && id.equals(((InsuranceTypeDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InsuranceTypeDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", charge=" + getCharge() +
            "}";
    }
}
