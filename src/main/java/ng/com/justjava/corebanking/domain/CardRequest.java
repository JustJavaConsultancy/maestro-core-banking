package ng.com.justjava.corebanking.domain;

import ng.com.justjava.corebanking.util.StringEncryptionConverter;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "card_request")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CardRequest extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "surname")
    private String surname;

    private String department;
    private String faculty;
    private String matricno;
    private String scheme;
    private String image;
    private String cardtype;
    private String customerid;

    @Column(name = "wallet_id")
    @Convert(converter = StringEncryptionConverter.class)
    private String walletId;

    @Column(name = "card_nuban")
    @Convert(converter = StringEncryptionConverter.class)
    private String cardNuban;

    @ManyToOne
    Profile owner;

    public Profile getOwner() {
        return owner;
    }

    public void setOwner(Profile owner) {
        this.owner = owner;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getWalletId() {
        return walletId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

    public String getCardNuban() {
        return cardNuban;
    }

    public void setCardNuban(String cardNuban) {
        this.cardNuban = cardNuban;
    }

    private String status = "NEW"; //NEW, REJECTED, APPROVED

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getMatricno() {
        return matricno;
    }

    public void setMatricno(String matricno) {
        this.matricno = matricno;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCardtype() {
        return cardtype;
    }

    public void setCardtype(String cardtype) {
        this.cardtype = cardtype;
    }

    public String getCustomerid() {
        return customerid;
    }

    public void setCustomerid(String customerid) {
        this.customerid = customerid;
    }

    @Column(name = "unique_identifier")
    @Convert(converter = StringEncryptionConverter.class)
    private String uniqueIdentifier;

    public String getUniqueIdentifier() {
        return uniqueIdentifier;
    }

    public void setUniqueIdentifier(String uniqueIdentifier) {
        this.uniqueIdentifier = uniqueIdentifier;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "CardRequest{" +
            "id=" + id +
            ", firstName='" + firstName + '\'' +
            ", surname='" + surname + '\'' +
            ", department='" + department + '\'' +
            ", faculty='" + faculty + '\'' +
            ", matricno='" + matricno + '\'' +
            ", scheme='" + scheme + '\'' +
            ", image='" + image + '\'' +
            ", cardtype='" + cardtype + '\'' +
            ", customerid='" + customerid + '\'' +
            ", walletId='" + walletId + '\'' +
            ", cardNuban='" + cardNuban + '\'' +
            ", owner=" + owner +
            ", status='" + status + '\'' +
            ", uniqueIdentifier='" + uniqueIdentifier + '\'' +
            "} " + super.toString();
    }
}
