package ng.com.justjava.corebanking.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "biller_custom_field_option")
@JsonIgnoreProperties(value = {"billerPlatform", "billerServiceOptions"})
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class BillerCustomFieldOption implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private long id;

    @OneToMany(mappedBy = "billerCustomFieldOption")
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = "billerCustomFieldOption", allowSetters = true)
    private Set<BillerServiceOption> billerServiceOptions = new HashSet<>();

    @Column(name = "has_price")
    private boolean hasFixedPrice;

    @Column(name = "currency")
    private String currency;

    @Column(name = "accept_part_payment")
    private boolean acceptPartPayment;

    @Column(name = "fixed_amount")
    private double fixedAmount;

    @OneToOne
    @JsonIgnoreProperties(value = "billerCustomFieldOption", allowSetters = true)
    private BillerPlatform billerPlatform;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public BillerPlatform getBillerPlatform() {
        return billerPlatform;
    }

    public void setBillerPlatform(BillerPlatform billerPlatform) {
        this.billerPlatform = billerPlatform;
    }

    public Set<BillerServiceOption> getBillerServiceOptions() {
        return billerServiceOptions;
    }

    public void setBillerServiceOptions(Set<BillerServiceOption> billerServiceOptions) {
        this.billerServiceOptions = billerServiceOptions;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public boolean isAcceptPartPayment() {
        return acceptPartPayment;
    }

    public void setAcceptPartPayment(boolean acceptPartPayment) {
        this.acceptPartPayment = acceptPartPayment;
    }

    public double getFixedAmount() {
        return fixedAmount;
    }

    public void setFixedAmount(double fixedAmount) {
        this.fixedAmount = fixedAmount;
    }

    public boolean isHasFixedPrice() {
        return hasFixedPrice;
    }

    public void setHasFixedPrice(boolean hasFixedPrice) {
        this.hasFixedPrice = hasFixedPrice;
    }

    public String getCurrency() {
        return currency;
    }

    public Set<BillerServiceOption> getCustomField() {
        return this.billerServiceOptions == null ? null : this.billerServiceOptions;
    }

    @Override
    public String toString() {
        return "BillerCustomFieldOption{" +
            "id=" + id +
            ", billerServiceOptions=" + billerServiceOptions +
            ", hasFixedPrice=" + hasFixedPrice +
            ", currency='" + currency + '\'' +
            ", acceptPartPayment=" + acceptPartPayment +
            ", fixedAmount=" + fixedAmount +
            ", billerPlatform=" + billerPlatform +
            '}';
    }
}
