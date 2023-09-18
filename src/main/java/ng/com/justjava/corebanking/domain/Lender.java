package ng.com.justjava.corebanking.domain;

import ng.com.justjava.corebanking.domain.enumeration.Tenure;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * A Lender.
 */
@Entity
@Table(name = "lender")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Lender implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "rate", nullable = false)
    private Double rate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "preferred_tenure", nullable = false)
    private Tenure preferredTenure;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private Profile profile;

    // jhipster-needle-entity-add-field - JHipster will add fields here
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

    public Lender rate(Double rate) {
        this.rate = rate;
        return this;
    }

    public Tenure getPreferredTenure() {
        return preferredTenure;
    }

    public void setPreferredTenure(Tenure preferredTenure) {
        this.preferredTenure = preferredTenure;
    }

    public Lender preferredTenure(Tenure preferredTenure) {
        this.preferredTenure = preferredTenure;
        return this;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Lender profile(Profile profile) {
        this.profile = profile;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Lender)) {
            return false;
        }
        return id != null && id.equals(((Lender) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Lender{" +
            "id=" + getId() +
            ", rate=" + getRate() +
            ", preferedTenure='" + getPreferredTenure() + "'" +
            "}";
    }
}
