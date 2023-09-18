package ng.com.justjava.corebanking.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;

/**
 * A BonusPoint.
 */
@Entity
@Table(name = "bonus_point")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class BonusPoint implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "remark")
    private String remark;

    @OneToOne(optional = false)
    @JoinColumn(unique = true)
    private Journal journal;

    @ManyToOne
    @JsonIgnoreProperties(value = "bonusPoints", allowSetters = true)
    private Profile profile;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public BonusPoint amount(Double amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getRemark() {
        return remark;
    }

    public BonusPoint remark(String remark) {
        this.remark = remark;
        return this;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Journal getJournal() {
        return journal;
    }

    public BonusPoint journal(Journal journal) {
        this.journal = journal;
        return this;
    }

    public void setJournal(Journal journal) {
        this.journal = journal;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public BonusPoint profile(Profile profile) {
        this.profile = profile;
        return this;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BonusPoint)) {
            return false;
        }
        return id != null && id.equals(((BonusPoint) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BonusPoint{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            ", remark='" + getRemark() + "'" +
            "}";
    }
}
