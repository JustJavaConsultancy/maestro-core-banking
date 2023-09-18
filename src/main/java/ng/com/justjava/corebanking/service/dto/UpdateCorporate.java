package ng.com.justjava.corebanking.service.dto;

public class UpdateCorporate {

    String phoneNumber;
    String newPhoneNumber;
    String newAddress;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getNewPhoneNumber() {
        return newPhoneNumber;
    }

    public void setNewPhoneNumber(String newPhoneNumber) {
        this.newPhoneNumber = newPhoneNumber;
    }

    public String getNewAddress() {
        return newAddress;
    }

    public void setNewAddress(String newAddress) {
        this.newAddress = newAddress;
    }

    @Override
    public String toString() {
        return "UpdateCorporate{" +
            "phoneNumber='" + phoneNumber + '\'' +
            ", newPhoneNumber='" + newPhoneNumber + '\'' +
            ", newAddress='" + newAddress + '\'' +
            '}';
    }
}
