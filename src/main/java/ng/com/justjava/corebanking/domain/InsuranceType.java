package ng.com.justjava.corebanking.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A InsuranceType.
 */
@Entity
@Table(name = "insurance_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class InsuranceType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "charge")
    private Double charge;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public InsuranceType name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getCharge() {
        return charge;
    }

    public InsuranceType charge(Double charge) {
        this.charge = charge;
        return this;
    }

    public void setCharge(Double charge) {
        this.charge = charge;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InsuranceType)) {
            return false;
        }
        return id != null && id.equals(((InsuranceType) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InsuranceType{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", charge=" + getCharge() +
            "}";
    }
}
