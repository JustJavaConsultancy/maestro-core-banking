package ng.com.systemspecs.apigateway.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ng.com.systemspecs.apigateway.domain.enumeration.Gender;
import ng.com.systemspecs.apigateway.domain.enumeration.KycRequestDocType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Past;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Profile of the user of the Solution this can be of any of the types :- Customer, Agent or Admin
 */
@Entity
@Table(name = "profile", indexes = {@Index(name = "profile", columnList = "phone_number")})
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Profile extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "profile_id")
    private String profileID;

    @Column(name = "xxxx")
    private String pin;

    @Column(name = "device_notification_token")
	private String deviceNotificationToken;


    @Column(name = "phone_number", unique=true)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "date_of_birth")
    @Past
    private LocalDate dateOfBirth;

    @Column(name = "address")
    private String address;

    @Column(name = "question")
    private String secretQuestion;

    @Column(name = "answer")
    private String secretAnswer;

    @Lob
    @Column(name = "photo")
    private byte[] photo;

    @Column(name = "photo_content_type")
    private String photoContentType;

    @Column(name = "bvn")
    private String bvn;

    @Column(name = "valid_id")
    private String validID;

    @Enumerated(EnumType.STRING)
    @Column(name = "valid_id_doc_type", nullable = true)
    private KycRequestDocType validDocType = KycRequestDocType.NIN;

    @Column(name = "nin")
    private String nin;

    @Column(name = "profile_picture")
    private String profilePicture;

    @Column(name = "total_bonus")
    private double totalBonus;

    @OneToMany(mappedBy = "accountOwner")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<WalletAccount> walletAccounts = new HashSet<>();

    @OneToMany(mappedBy = "profile")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<MyDevice> myDevices = new HashSet<>();

    @OneToMany(mappedBy = "transactionOwner")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<PaymentTransaction> paymentTransactions = new HashSet<>();

    @OneToMany(mappedBy = "phoneNumber")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<BillerTransaction> billerTransactions = new HashSet<>();

    @OneToMany(mappedBy = "phoneNumber")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Customersubscription> customersubscriptions = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = "profiles", allowSetters = true)
    private User user;

    @OneToMany(mappedBy = "profile")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<BonusPoint> bonusPoints = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = "profiles", allowSetters = true)
    private ApprovalGroup approvalGroup;

    @ManyToOne
    @JsonIgnoreProperties(value = "profiles", allowSetters = true)
    private ProfileType profileType;

    @ManyToOne
    @JsonIgnoreProperties(value = "profiles", allowSetters = true)
    private Kyclevel kyc;

    @OneToMany(mappedBy = "accountOwner")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Beneficiary> beneficiaries = new HashSet<>();

    @OneToMany(mappedBy = "addressOwner")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Address> addresses = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeviceNotificationToken() {
		return deviceNotificationToken;
	}

	public void setDeviceNotificationToken(String deviceNotificationToken) {
		this.deviceNotificationToken = deviceNotificationToken;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

    public String getProfileID() {
        return profileID;
    }

    public Profile profileID(String profileID) {
        this.profileID = profileID;
        return this;
    }

    public void setProfileID(String profileID) {
        this.profileID = profileID;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Profile phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Gender getGender() {
        return gender;
    }

    public Profile gender(Gender gender) {
        this.gender = gender;
        return this;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public Profile dateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        return this;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Profile address(String address) {
        this.address = address;
        return this;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public Profile photo(byte[] photo) {
        this.photo = photo;
        return this;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getPhotoContentType() {
        return photoContentType;
    }

    public Profile photoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
        return this;
    }

    public void setPhotoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
    }

    public String getBvn() {
        return bvn;
    }

    public Profile bvn(String bvn) {
        this.bvn = bvn;
        return this;
    }

    public void setBvn(String bvn) {
        this.bvn = bvn;
    }

    public String getValidID() {
        return validID;
    }

    public Profile validID(String validID) {
        this.validID = validID;
        return this;
    }

    public void setValidID(String validID) {
        this.validID = validID;
    }

    public KycRequestDocType getValidDocType() {
		return validDocType;
	}

	public void setValidDocType(KycRequestDocType validDocType) {
		this.validDocType = validDocType;
	}

	public Set<WalletAccount> getWalletAccounts() {
        return walletAccounts;
    }

    public Set<MyDevice> getMyDevices() {
        return myDevices;
    }

    public String getFullName() {
        if (getUser() != null) {
            return getUser().getLastName() + " " + getUser().getFirstName();
        }
        return "";
    }

    public Set<BonusPoint> getBonusPoints() {
        return bonusPoints;
    }

    public void setBonusPoints(Set<BonusPoint> bonusPoints) {
        this.bonusPoints = bonusPoints;
    }

    public void setWalletAccounts(Set<WalletAccount> walletAccounts) {
        this.walletAccounts = walletAccounts;
    }

    public Profile walletAccounts(Set<WalletAccount> walletAccounts) {
        this.walletAccounts = walletAccounts;
        return this;
    }

    public Profile addWalletAccount(WalletAccount walletAccount) {
        this.walletAccounts.add(walletAccount);
        walletAccount.setAccountOwner(this);
        return this;
    }

    public Profile removeWalletAccount(WalletAccount walletAccount) {
        this.walletAccounts.remove(walletAccount);
        walletAccount.setAccountOwner(null);
        return this;
    }

    public void setMyDevices(Set<MyDevice> myDevices) {
        this.myDevices = myDevices;
    }

    public Profile myDevices(Set<MyDevice> myDevices) {
        this.myDevices = myDevices;
        return this;
    }

    public Profile addMyDevices(MyDevice myDevice) {
        this.myDevices.add(myDevice);
        myDevice.setProfile(this);
        return this;
    }

    public Profile removeMyDevice(MyDevice myDevice) {
        this.myDevices.remove(myDevice);
        myDevice.setProfile(null);
        return this;
    }

    public Set<PaymentTransaction> getPaymentTransactions() {
        return paymentTransactions;
    }

    public Profile paymentTransactions(Set<PaymentTransaction> paymentTransactions) {
        this.paymentTransactions = paymentTransactions;
        return this;
    }

    public Profile addPaymentTransaction(PaymentTransaction paymentTransaction) {
        this.paymentTransactions.add(paymentTransaction);
        paymentTransaction.setTransactionOwner(this);
        return this;
    }

    public Profile removePaymentTransaction(PaymentTransaction paymentTransaction) {
        this.paymentTransactions.remove(paymentTransaction);
        paymentTransaction.setTransactionOwner(null);
        return this;
    }

    public void setPaymentTransactions(Set<PaymentTransaction> paymentTransactions) {
        this.paymentTransactions = paymentTransactions;
    }

    public Set<BillerTransaction> getBillerTransactions() {
        return billerTransactions;
    }

    public Profile billerTransactions(Set<BillerTransaction> billerTransactions) {
        this.billerTransactions = billerTransactions;
        return this;
    }

    public Profile addBillerTransaction(BillerTransaction billerTransaction) {
        this.billerTransactions.add(billerTransaction);
        billerTransaction.setPhoneNumber(this);
        return this;
    }

    public Profile removeBillerTransaction(BillerTransaction billerTransaction) {
        this.billerTransactions.remove(billerTransaction);
        billerTransaction.setPhoneNumber(null);
        return this;
    }

    public void setBillerTransactions(Set<BillerTransaction> billerTransactions) {
        this.billerTransactions = billerTransactions;
    }

    public Set<Customersubscription> getCustomersubscriptions() {
        return customersubscriptions;
    }

    public Profile customersubscriptions(Set<Customersubscription> customersubscriptions) {
        this.customersubscriptions = customersubscriptions;
        return this;
    }

    public Profile addCustomersubscription(Customersubscription customersubscription) {
        this.customersubscriptions.add(customersubscription);
        customersubscription.setPhoneNumber(this);
        return this;
    }

    public Profile removeCustomersubscription(Customersubscription customersubscription) {
        this.customersubscriptions.remove(customersubscription);
        customersubscription.setPhoneNumber(null);
        return this;
    }

    public void setCustomersubscriptions(Set<Customersubscription> customersubscriptions) {
        this.customersubscriptions = customersubscriptions;
    }

    public User getUser() {
        return user;
    }

    public Profile user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ProfileType getProfileType() {
        return profileType;
    }

    public Profile profileType(ProfileType profileType) {
        this.profileType = profileType;
        return this;
    }

    public void setProfileType(ProfileType profileType) {
        this.profileType = profileType;
    }

    public Kyclevel getKyc() {
        return kyc;
    }

    public Profile kyc(Kyclevel kyclevel) {
        this.kyc = kyclevel;
        return this;
    }

    public void setKyc(Kyclevel kyclevel) {
        this.kyc = kyclevel;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public Set<Beneficiary> getBeneficiaries() {
        return beneficiaries;
    }

    public void setBeneficiaries(Set<Beneficiary> beneficiaries) {
        this.beneficiaries = beneficiaries;
    }

    public Instant getCreatedDate() {
        Instant createdDate = null;
        if (user != null) {
            createdDate = user.getCreatedDate();
        }
        return createdDate;
    }

    public ApprovalGroup getApprovalGroup() {
        return approvalGroup;
    }

    public Profile approvalGroup(ApprovalGroup approvalGroup) {
        this.approvalGroup = approvalGroup;
        return this;
    }

    public void setApprovalGroup(ApprovalGroup approvalGroup) {
        this.approvalGroup = approvalGroup;
    }

    public Set<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(Set<Address> addresses) {
        this.addresses = addresses;
    }

    public String getSecretQuestion() {
        return secretQuestion;
    }

    public void setSecretQuestion(String secretQuestion) {
        this.secretQuestion = secretQuestion;
    }

    public String getSecretAnswer() {
        return secretAnswer;
    }

    public void setSecretAnswer(String secretAnswer) {
        this.secretAnswer = secretAnswer;
    }

    public double getTotalBonus() {
        return totalBonus;
    }

    public void setTotalBonus(double totalBonus) {
        this.totalBonus = totalBonus;
    }

    public String getNin() {
        return nin;
    }

    public void setNin(String nin) {
        this.nin = nin;
    }

    private String getEmail() {
        if (user != null) {
            return user.getEmail();
        }
        return "";
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (!(o instanceof Profile)) {
            return false;
        }

        return id != null && id.equals(((Profile) o).id);

    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {

        return "Profile{" +
            "id=" + getId() +
            ", profileID='" + getProfileID() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", gender='" + getGender() + "'" +
            ", dateOfBirth='" + getDateOfBirth() + "'" +
            ", photoContentType='" + getPhotoContentType() + "'" +
            ", bvn='" + getBvn() + "'" +
            ", validID='" + getValidID() + "'" +
            ", approvaGroup='" + approvalGroup + "'" +
            ", deviceNotificationToken='" + getDeviceNotificationToken() + "'" +
            ", secretQuestion='" + secretQuestion + "'" +
            ", secretAnswer='" + secretAnswer + "'" +
            ", totalBonus='" + totalBonus + "'" +
            ", nin='" + nin + "'" +
            ", email='" + getEmail() + "'" +
            ", profilePicture='" + profilePicture + "'" +
            "}";
    }
}
