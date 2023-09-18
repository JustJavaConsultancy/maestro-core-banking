package ng.com.systemspecs.apigateway.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * A SweepingConfig.
 */
@Entity
@Table(name = "sweeping_config")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SweepingConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "vendor_name", nullable = false)
    private String vendorName;

    @NotNull
    @Column(name = "source_account", nullable = false)
    private String sourceAccount;

    @NotNull
    @Column(name = "destination_bank_code", nullable = false)
    private String destinationBankCode;

    @Column(name = "destination_account")
    private String destinationAccount;

    @NotNull
    @DecimalMin(value = "1")
    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "percentage")
    private Double percentage;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public SweepingConfig vendorName(String vendorName) {
        this.vendorName = vendorName;
        return this;
    }

    public String getSourceAccount() {
        return sourceAccount;
    }

    public void setSourceAccount(String sourceAccount) {
        this.sourceAccount = sourceAccount;
    }

    public SweepingConfig sourceAccount(String sourceAccount) {
        this.sourceAccount = sourceAccount;
        return this;
    }

    public String getDestinationBankCode() {
        return destinationBankCode;
    }

    public void setDestinationBankCode(String destinationBankCode) {
        this.destinationBankCode = destinationBankCode;
    }

    public SweepingConfig destinationBankCode(String destinationBankCode) {
        this.destinationBankCode = destinationBankCode;
        return this;
    }

    public String getDestinationAccount() {
        return destinationAccount;
    }

    public void setDestinationAccount(String destinationAccount) {
        this.destinationAccount = destinationAccount;
    }

    public SweepingConfig destinationAccount(String destinationAccount) {
        this.destinationAccount = destinationAccount;
        return this;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public SweepingConfig amount(Double amount) {
        this.amount = amount;
        return this;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    public SweepingConfig percentage(Double percentage) {
        this.percentage = percentage;
        return this;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SweepingConfig)) {
            return false;
        }
        return id != null && id.equals(((SweepingConfig) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SweepingConfig{" +
            "id=" + getId() +
            ", vendorName='" + getVendorName() + "'" +
            ", sourceAccount='" + getSourceAccount() + "'" +
            ", destinationBankCode='" + getDestinationBankCode() + "'" +
            ", destinationAccount='" + getDestinationAccount() + "'" +
            ", amount=" + getAmount() +
            ", percentage=" + getPercentage() +
            "}";
    }
}
