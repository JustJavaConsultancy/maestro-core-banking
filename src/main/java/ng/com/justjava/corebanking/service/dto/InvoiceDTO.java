package ng.com.justjava.corebanking.service.dto;

public class InvoiceDTO {
    private String reference;
    private String action;
    private String pin;
    private String accountNumber;

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    @Override
    public String toString() {
        return "InvoiceDTO{" +
            "reference='" + reference + '\'' +
            ", action='" + action + '\'' +
            ", pin='" + pin + '\'' +
            ", accountNumber='" + accountNumber + '\'' +
            '}';
    }
}
