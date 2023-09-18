package ng.com.justjava.corebanking.service.dto;

public class PolarisCardOperationsDTO {

    private String accountNumber;
    private String bin;
    private String cardType;
    private String confirmNewPin;
    private String expiryDate;
    private String last4;
    private String newPin;
    private String oldPin;

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

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getConfirmNewPin() {
        return confirmNewPin;
    }

    public void setConfirmNewPin(String confirmNewPin) {
        this.confirmNewPin = confirmNewPin;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getLast4() {
        return last4;
    }

    public void setLast4(String last4) {
        this.last4 = last4;
    }

    public String getNewPin() {
        return newPin;
    }

    public void setNewPin(String newPin) {
        this.newPin = newPin;
    }

    public String getOldPin() {
        return oldPin;
    }

    public void setOldPin(String oldPin) {
        this.oldPin = oldPin;
    }

    @Override
    public String toString() {
        return "PolarisCardOperationsDTO{" +
            "accountNumber='" + accountNumber + '\'' +
            ", bin='" + bin + '\'' +
            ", cardType='" + cardType + '\'' +
            ", confirmNewPin='" + confirmNewPin + '\'' +
            ", expiryDate='" + expiryDate + '\'' +
            ", last4='" + last4 + '\'' +
            ", newPin='" + newPin + '\'' +
            ", oldPin='" + oldPin + '\'' +
            '}';
    }
}
