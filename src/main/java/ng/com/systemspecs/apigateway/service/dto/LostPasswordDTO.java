package ng.com.systemspecs.apigateway.service.dto;


import javax.validation.constraints.NotBlank;

public class LostPasswordDTO {
    @NotBlank(message = "Phone Number Required")
    private String phoneNumber;
    @NotBlank(message = "New Password is Required")
    private String newPassword;

    private String pin;

    public LostPasswordDTO(String phoneNumber, String newPassword, String pin) {
        this.phoneNumber = phoneNumber;
        this.newPassword = newPassword;
        this.pin = pin;
    }

    public LostPasswordDTO() {
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    @Override
    public String toString() {
        return "LostPasswordDTO{" +
            "phoneNumber='" + phoneNumber + '\'' +
            ", newPassword='" + newPassword + '\'' +
            ", pin='" + pin + '\'' +
            '}';
    }
}
