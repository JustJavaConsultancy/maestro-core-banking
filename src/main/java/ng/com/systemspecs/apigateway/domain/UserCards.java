package ng.com.systemspecs.apigateway.domain;

import ng.com.systemspecs.apigateway.util.StringEncryptionConverter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "user_cards")
public class UserCards extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "pan")
    @Convert(converter = StringEncryptionConverter.class)
    String pan;

    @Column(name = "bin")
    @Convert(converter = StringEncryptionConverter.class)
    String bin;

    @Column(name = "last_4")
    @Convert(converter = StringEncryptionConverter.class)
    String last4;

    @Column(name = "account_number")
    @Convert(converter = StringEncryptionConverter.class)
    String accountNumber;

    @Column(name = "card_name")
    String cardName;

    @Column(name = "card_type")
    String cardType;

    @Column(name = "provider")
    String provider;

    @Column(name = "scheme")
    String scheme;

    @Column(name = "expiry_date")
    String expiryDate;

    @Column(name = "current_balance")
    @Convert(converter = StringEncryptionConverter.class)
    private String currentBalance;

    @ManyToOne
    Profile owner;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getBin() {
        return bin;
    }

    public void setBin(String bin) {
        this.bin = bin;
    }

    public String getLast4() {
        return last4;
    }

    public void setLast4(String last4) {
        this.last4 = last4;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
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

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public Profile getOwner() {
        return owner;
    }

    public void setOwner(Profile owner) {
        this.owner = owner;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(String currentBalance) {
        this.currentBalance = currentBalance;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    @Column(name = "status")
    private String status = "INACTIVE";

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    @Override
    public String toString() {
        return "UserCards{" +
            "id=" + id +
            ", pan='" + pan + '\'' +
            ", bin='" + bin + '\'' +
            ", last4='" + last4 + '\'' +
            ", accountNumber='" + accountNumber + '\'' +
            ", cardName='" + cardName + '\'' +
            ", cardType='" + cardType + '\'' +
            ", provider='" + provider + '\'' +
            ", scheme='" + scheme + '\'' +
            ", expiryDate='" + expiryDate + '\'' +
            ", currentBalance='" + currentBalance + '\'' +
            ", owner=" + owner +
            ", uniqueIdentifier='" + uniqueIdentifier + '\'' +
            ", status='" + status + '\'' +
            "} " + super.toString();
    }
}
