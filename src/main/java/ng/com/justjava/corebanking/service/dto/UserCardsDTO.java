package ng.com.justjava.corebanking.service.dto;



import java.io.Serializable;

public class UserCardsDTO implements Serializable {

    private Long id;
    private String pan;
    private String bin;
    private String last4;
    private String accountNumber;
    private String uniqueidentifier;
    private String cardName;
    private String cardType;
    private String provider;
    private String expiryDate;
    private String scheme;
    private ProfileDTO owner;

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

    public String getUniqueidentifier() {
        return uniqueidentifier;
    }

    public void setUniqueidentifier(String uniqueidentifier) {
        this.uniqueidentifier = uniqueidentifier;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
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

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public ProfileDTO getOwner() {
        return owner;
    }

    public void setOwner(ProfileDTO owner) {
        this.owner = owner;
    }

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
        return "UserCardsDTO{" +
            "id=" + id +
            ", pan='" + pan + '\'' +
            ", bin='" + bin + '\'' +
            ", last4='" + last4 + '\'' +
            ", accountNumber='" + accountNumber + '\'' +
            ", uniqueidentifier='" + uniqueidentifier + '\'' +
            ", cardName='" + cardName + '\'' +
            ", cardType='" + cardType + '\'' +
            ", provider='" + provider + '\'' +
            ", expiryDate='" + expiryDate + '\'' +
            ", scheme='" + scheme + '\'' +
            ", owner=" + owner +
            ", status='" + status + '\'' +
            '}';
    }
}
