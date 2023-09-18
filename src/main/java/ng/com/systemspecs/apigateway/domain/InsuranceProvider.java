package ng.com.systemspecs.apigateway.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * A InsuranceProvider.
 */
@Entity
@Table(name = "insurance_provider")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class InsuranceProvider implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "rate", nullable = false)
    private Double rate;

    @NotNull
    @Column(name = "preferred_tenure", nullable = false)
    private String preferred_tenure;

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

    public InsuranceProvider rate(Double rate) {
        this.rate = rate;
        return this;
    }

    public String getPreferred_tenure() {
        return preferred_tenure;
    }

    public void setPreferred_tenure(String preferred_tenure) {
        this.preferred_tenure = preferred_tenure;
    }

    public InsuranceProvider preferred_tenure(String preferred_tenure) {
        this.preferred_tenure = preferred_tenure;
        return this;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public InsuranceProvider profile(Profile profile) {
        this.profile = profile;
        return this;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InsuranceProvider)) {
            return false;
        }
        return id != null && id.equals(((InsuranceProvider) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InsuranceProvider{" +
            "id=" + getId() +
            ", rate=" + getRate() +
            ", preferred_tenure='" + getPreferred_tenure() + "'" +
            "}";
    }
}
