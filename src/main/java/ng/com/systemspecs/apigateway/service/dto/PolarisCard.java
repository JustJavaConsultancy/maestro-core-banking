package ng.com.systemspecs.apigateway.service.dto;

public class PolarisCard {
    private String name;
    private String accountNumber;
    private String bin;
    private String last4;
    private String expiryDate;
    private String status;
    private String message;
    private String cardType;
    private String pan;

    @Override
    public String toString() {
        return "PolarisCard{" +
            "name:'" + name + '\'' +
            ", accountNumber:'" + accountNumber + '\'' +
            ", bin:'" + bin + '\'' +
            ", last4:'" + last4 + '\'' +
            ", expiryDate:'" + expiryDate + '\'' +
            ", status:'" + status + '\'' +
            ", message:'" + message + '\'' +
            ", cardType:'" + cardType + '\'' +
            ", pan:'" + pan + '\'' +
            '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
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

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public PolarisCard(String name, String accountNumber, String bin, String last4, String expiryDate, String status, String message, String cardType, String pan) {
        this.name = name;
        this.accountNumber = accountNumber;
        this.bin = bin;
        this.last4 = last4;
        this.expiryDate = expiryDate;
        this.status = status;
        this.message = message;
        this.cardType = cardType;
        this.pan = pan;
    }
}
