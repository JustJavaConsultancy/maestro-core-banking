package ng.com.justjava.corebanking.domain;

import ng.com.justjava.corebanking.domain.enumeration.CorporateProfileRegistrationType;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cache;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Profile of the Corporate account user
 */

@Entity
@Table(name = "corporate_profile", indexes = {@Index(columnList = "phone_number"), @Index(columnList = "rc_no")})
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CorporateProfile extends AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "business_name", nullable = false)
    String name;

    @Column(name = "business_address")
    String address;

    @Enumerated(EnumType.STRING)
    @Column(name = "registration_type", nullable = false)
    CorporateProfileRegistrationType regType = CorporateProfileRegistrationType.REGISTERED_BUSINESS_NAME;

    @Column(name = "business_category", nullable = false)
    String category;

    @Column(name = "rc_no", nullable = false)
    String rcNO;

    @Column(name = "phone_number")
    String phoneNo;

    @Column(name = "tin", nullable = false)
    String tin;

    @Column(name = "cac_certificate")
    boolean cacCertificate = false;

    @Column(name = "cac_co7")
    boolean cacCo7 = false;

    @Column(name = "cac_co2")
    boolean cacCo2 = false;

    @Column(name = "utility_bill")
    boolean utilityBill = false;

    @ManyToOne
    Profile profile;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public CorporateProfileRegistrationType getRegType() {
        return regType;
    }

    public void setRegType(CorporateProfileRegistrationType regType) {
        this.regType = regType;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getRcNO() {
        return rcNO;
    }

    public void setRcNO(String rcNO) {
        this.rcNO = rcNO;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getTin() {
        return tin;
    }

    public void setTin(String tin) {
        this.tin = tin;
    }

    public boolean isCacCertificate() {
        return cacCertificate;
    }

    public void setCacCertificate(boolean cacCertificate) {
        this.cacCertificate = cacCertificate;
    }

    public boolean isCacCo7() {
        return cacCo7;
    }

    public void setCacCo7(boolean cacCo7) {
        this.cacCo7 = cacCo7;
    }

    public boolean isCacCo2() {
        return cacCo2;
    }

    public void setCacCo2(boolean cacCo2) {
        this.cacCo2 = cacCo2;
    }

    public boolean isUtilityBill() {
        return utilityBill;
    }

    public void setUtilityBill(boolean utilityBill) {
        this.utilityBill = utilityBill;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public CorporateProfile name(String name){
        this.name = name;
        return this;
    }

    public CorporateProfile rcNo(String rcNO){
        this.rcNO = rcNO;
        return this;
    }

    @Override
    public String toString() {
        return "CorporateProfile{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", address='" + address + '\'' +
            ", regType='" + regType + '\'' +
            ", category='" + category + '\'' +
            ", rcNO='" + rcNO + '\'' +
            ", phoneNo='" + phoneNo + '\'' +
            ", tin='" + tin + '\'' +
            ", cacCertificate=" + cacCertificate +
            ", cacCo7=" + cacCo7 +
            ", cacCo2=" + cacCo2 +
            ", utilityBill=" + utilityBill +
            ", profile=" + profile +
            '}';
    }


    public String toString2() {
        return "CorporateProfile{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", address='" + address + '\'' +
            ", regType='" + regType + '\'' +
            ", category='" + category + '\'' +
            ", rcNO='" + rcNO + '\'' +
            ", phoneNo='" + phoneNo + '\'' +
            ", tin='" + tin + '\'' +
            ", cacCertificate=" + cacCertificate +
            ", cacCo7=" + cacCo7 +
            ", cacCo2=" + cacCo2 +
            ", utilityBill=" + utilityBill +
            '}';
    }
}
