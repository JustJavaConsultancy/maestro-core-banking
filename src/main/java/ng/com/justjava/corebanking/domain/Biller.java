package ng.com.justjava.corebanking.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * This setup different Billers in the system a Customer can be in
 */
@Entity
@Table(name = "biller")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Biller implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "biller_id")
    private String billerID;

    @Column(name = "biller")
    private String biller;

    @Column(name = "address")
    private String address;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "is_popular")
    private boolean isPopular;

    @OneToMany(mappedBy = "biller")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Customersubscription> customersubscriptions = new HashSet<>();

    @OneToMany(mappedBy = "biller")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<BillerPlatform> billerPlatforms = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = "billers", allowSetters = true)
    private BillerCategory billerCategory;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBillerID() {
        return billerID;
    }

    public void setBillerID(String billerID) {
        this.billerID = billerID;
    }

    public Biller billerID(String billerID) {
        this.billerID = billerID;
        return this;
    }

    public String getBiller() {
        return biller;
    }

    public Biller biller(String biller) {
        this.biller = biller;
        return this;
    }

    public void setBiller(String biller) {
        this.biller = biller;
    }

    public String getAddress() {
        return address;
    }

    public Biller address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Biller phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Set<Customersubscription> getCustomersubscriptions() {
        return customersubscriptions;
    }

    public Biller customersubscriptions(Set<Customersubscription> customersubscriptions) {
        this.customersubscriptions = customersubscriptions;
        return this;
    }

    public Biller addCustomersubscription(Customersubscription customersubscription) {
        this.customersubscriptions.add(customersubscription);
        customersubscription.setBiller(this);
        return this;
    }

    public Biller removeCustomersubscription(Customersubscription customersubscription) {
        this.customersubscriptions.remove(customersubscription);
        customersubscription.setBiller(null);
        return this;
    }

    public void setCustomersubscriptions(Set<Customersubscription> customersubscriptions) {
        this.customersubscriptions = customersubscriptions;
    }

    public Set<BillerPlatform> getBillerPlatforms() {
        return billerPlatforms;
    }

    public Biller billerPlatforms(Set<BillerPlatform> billerPlatforms) {
        this.billerPlatforms = billerPlatforms;
        return this;
    }

    public Biller addBillerPlatform(BillerPlatform billerPlatform) {
        this.billerPlatforms.add(billerPlatform);
        billerPlatform.setBiller(this);
        return this;
    }

    public Biller removeBillerPlatform(BillerPlatform billerPlatform) {
        this.billerPlatforms.remove(billerPlatform);
        billerPlatform.setBiller(null);
        return this;
    }

    public void setBillerPlatforms(Set<BillerPlatform> billerPlatforms) {
        this.billerPlatforms = billerPlatforms;
    }

    public BillerCategory getBillerCategory() {
        return billerCategory;
    }

    public Biller billerCategory(BillerCategory billerCategory) {
        this.billerCategory = billerCategory;
        return this;
    }

    public void setBillerCategory(BillerCategory billerCategory) {
        this.billerCategory = billerCategory;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public boolean isPopular() {
        return isPopular;
    }

    public void setPopular(boolean popular) {
        isPopular = popular;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Biller)) {
            return false;
        }
        return id != null && id.equals(((Biller) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }


    // prettier-ignore
    @Override
    public String toString() {
        return "Biller{" +
            "id=" + getId() +
            ", billerID=" + getBillerID() +
            ", biller='" + getBiller() + "'" +
            ", address='" + getAddress() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", isPopular='" + isPopular() + "'" +
            "}";
    }
}
