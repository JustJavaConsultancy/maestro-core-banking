package ng.com.justjava.corebanking.service.dto;

import javax.validation.constraints.NotBlank;

public class ChangePhonenumberDTO {
    @NotBlank(message="Old Phone Number Required")
    private String oldPhoneNumber;
    @NotBlank(message="New Phone Number is Required")
    private String newPhoneNumber;


    public String getOldPhoneNumber() {
        return oldPhoneNumber;
    }
    public void setOldPhoneNumber(String oldPhoneNumber) {
        this.oldPhoneNumber = oldPhoneNumber;
    }
    public String getNewPhoneNumber() {
        return newPhoneNumber;
    }
    public void setNewPhoneNumber(String newPhoneNumber) {
        this.newPhoneNumber = newPhoneNumber;
    }

    @Override
    public String toString() {
        return "ChangePhonenumberDTO{" +
            "oldPhoneNumber='" + oldPhoneNumber + '\'' +
            ", newPhoneNumber='" + newPhoneNumber + '\'' +
            '}';
    }
}
