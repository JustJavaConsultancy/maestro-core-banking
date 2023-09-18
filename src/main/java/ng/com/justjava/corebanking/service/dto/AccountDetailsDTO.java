package ng.com.justjava.corebanking.service.dto;

public class AccountDetailsDTO {

    private String accountName;
    private String nubanAccountNumber;
    private String trackingRef;
    private String phoneNumber;

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getNubanAccountNumber() {
        return nubanAccountNumber;
    }

    public void setNubanAccountNumber(String nubanAccountNumber) {
        this.nubanAccountNumber = nubanAccountNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getTrackingRef() {
        return trackingRef;
    }

    public void setTrackingRef(String trackingRef) {
        this.trackingRef = trackingRef;
    }

    @Override
    public String toString() {
        return "AccountDetailsDTO{" +
            "accountName='" + accountName + '\'' +
            ", cashConnectAccount='" + nubanAccountNumber + '\'' +
            ", phoneNumber='" + phoneNumber + '\'' +
            ", trackingRef='" + trackingRef + '\'' +
            '}';
    }
}
