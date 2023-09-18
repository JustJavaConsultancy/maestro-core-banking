package ng.com.justjava.corebanking.service.dto;

/**
 * A DTO representing a password change required data - current and new password.
 */
public class ChangePinDTO {
    private String currentPin;
    private String newPin;

    public ChangePinDTO() {
        // Empty constructor needed for Jackson.
    }

    public ChangePinDTO(String currentPin, String newPin) {
        this.currentPin = currentPin;
        this.newPin = newPin;
    }

    public String getCurrentPin() {

        return currentPin;
    }

    public void setCurrentPin(String currentPin) {
        this.currentPin = currentPin;
    }

    public String getNewPin() {
        return newPin;
    }

    public void setNewPin(String newPin) {
        this.newPin = newPin;
    }
}
