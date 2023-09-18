package ng.com.justjava.corebanking.service.dto;

import java.io.Serializable;
import java.time.Instant;

public class CardRequestDTO implements Serializable {
    private Long id;
    private String walletId;
    private String cardNuban;
    private String department;
    private String faculty;
    private String matricno;
    private String scheme;
    private String image;
    private String cardtype;
    private String customerid;
    private String uniqueIdentifier;
    private String status;
    private String firstName;
    private String surname;

    ProfileDTO owner;

    public ProfileDTO getOwner() {
        return owner;
    }

    public void setOwner(ProfileDTO owner) {
        this.owner = owner;
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

    public String getWalletId() {
        return walletId;
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

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
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

    public String getUniqueIdentifier() {
        return uniqueIdentifier;
    }

    public void setUniqueIdentifier(String uniqueIdentifier) {
        this.uniqueIdentifier = uniqueIdentifier;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Instant createdDate;

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return "CardRequestDTO{" +
            "id=" + id +
            ", walletId='" + walletId + '\'' +
            ", cardNuban='" + cardNuban + '\'' +
            ", department='" + department + '\'' +
            ", faculty='" + faculty + '\'' +
            ", matricno='" + matricno + '\'' +
            ", scheme='" + scheme + '\'' +
            ", image='" + image + '\'' +
            ", cardtype='" + cardtype + '\'' +
            ", customerid='" + customerid + '\'' +
            ", uniqueIdentifier='" + uniqueIdentifier + '\'' +
            ", status='" + status + '\'' +
            ", firstName='" + firstName + '\'' +
            ", surname='" + surname + '\'' +
            ", owner=" + owner +
            ", createdDate=" + createdDate +
            '}';
    }
}
