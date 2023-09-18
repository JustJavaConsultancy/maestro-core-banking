package ng.com.justjava.corebanking.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * A BillerPlatform.
 */
@Entity
@Table(name = "biller_platform")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonIgnoreProperties(value = {"biller"})
public class BillerPlatform implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @JsonIgnore
    @Column(name = "value_name")
    private String valueName;

    @JsonIgnore
    @Column(name = "value_id")
    private String valueID;

    @Column(name = "billerplatform_id")
    private Long billerplatformID;

    @Column(name = "biller_platform")
    private String billerPlatform;

    @JsonIgnore
    @Column(name = "amount")
    private Double amount;

    @ManyToOne
    @JsonIgnoreProperties(value = "billerPlatforms", allowSetters = true)
    private Biller biller;

    @OneToOne(mappedBy = "billerPlatform")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private BillerCustomFieldOption billerCustomFieldOption;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBillerplatformID() {
        return billerplatformID;
    }

    public BillerPlatform billerplatformID(Long billerplatformID) {
        this.billerplatformID = billerplatformID;
        return this;
    }

    public void setBillerplatformID(Long billerplatformID) {
        this.billerplatformID = billerplatformID;
    }

    public String getBillerPlatform() {
        return billerPlatform;
    }

    public BillerPlatform billerPlatform(String billerPlatform) {
        this.billerPlatform = billerPlatform;
        return this;
    }

    public void setBillerPlatform(String billerPlatform) {
        this.billerPlatform = billerPlatform;
    }

    public Double getAmount() {
        return amount;
    }

    public BillerPlatform amount(Double amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Biller getBiller() {
        return biller;
    }

    public BillerPlatform biller(Biller biller) {
        this.biller = biller;
        return this;
    }

    public void setBiller(Biller biller) {
        this.biller = biller;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here


    public Set<BillerServiceOption> getCustomField() {
        if (billerCustomFieldOption != null) {
            return billerCustomFieldOption.getCustomField();
        }
        return null;
    }

    @JsonIgnore
    public BillerCustomFieldOption getBillerCustomFieldOption() {
        return billerCustomFieldOption;
    }

    public void setBillerCustomFieldOption(BillerCustomFieldOption billerCustomFieldOption) {
        this.billerCustomFieldOption = billerCustomFieldOption;
    }

    public void setBillerCustomFieldOptions(BillerCustomFieldOption billerCustomFieldOption) {
        this.billerCustomFieldOption = billerCustomFieldOption;
    }

    public double getFixedAmount() {
        return this.billerCustomFieldOption == null ? 0.0 : this.billerCustomFieldOption.getFixedAmount();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BillerPlatform)) {
            return false;
        }
        return id != null && id.equals(((BillerPlatform) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BillerPlatform{" +
            "id=" + getId() +
            ", billerplatformID=" + getBillerplatformID() +
            ", billerPlatform='" + getBillerPlatform() + "'" +
            ", amount=" + getAmount() +
            "}";
    }

	public String getValueName() {
		return valueName;
	}

	public void setValueName(String valueName) {
		this.valueName = valueName;
	}

	public String getValueID() {
		return valueID;
	}

	public void setValueID(String valueID) {
		this.valueID = valueID;
	}
}
