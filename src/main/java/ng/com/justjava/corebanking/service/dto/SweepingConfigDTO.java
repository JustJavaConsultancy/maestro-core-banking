package ng.com.justjava.corebanking.service.dto;

import ng.com.justjava.corebanking.domain.SweepingConfig;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * A DTO for the {@link SweepingConfig} entity.
 */
public class SweepingConfigDTO implements Serializable {

    private Long id;

    @NotNull
    private String vendorName;

    @NotNull
    private String sourceAccount;

    @NotNull
    private String destinationBankCode;

    private String destinationAccount;

    @NotNull
    @DecimalMin(value = "1")
    private Double amount;

    private Double percentage;


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

    public String getSourceAccount() {
        return sourceAccount;
    }

    public void setSourceAccount(String sourceAccount) {
        this.sourceAccount = sourceAccount;
    }

    public String getDestinationBankCode() {
        return destinationBankCode;
    }

    public void setDestinationBankCode(String destinationBankCode) {
        this.destinationBankCode = destinationBankCode;
    }

    public String getDestinationAccount() {
        return destinationAccount;
    }

    public void setDestinationAccount(String destinationAccount) {
        this.destinationAccount = destinationAccount;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SweepingConfigDTO)) {
            return false;
        }

        return id != null && id.equals(((SweepingConfigDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SweepingConfigDTO{" +
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
