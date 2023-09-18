package ng.com.justjava.corebanking.service.dto;

public class SchemePinPasswordImageDTO {

    private String pin;
    private String password;
    private String image;
    private String phoneNumber;

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "SchemePinPasswordImageDTO{" +
            "pin='" + pin + '\'' +
            ", password='" + password + '\'' +
            ", image='" + image + '\'' +
            ", phoneNumber='" + phoneNumber + '\'' +
            '}';
    }
}
