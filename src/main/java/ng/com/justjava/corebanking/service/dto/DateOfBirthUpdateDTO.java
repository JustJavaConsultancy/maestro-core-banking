package ng.com.justjava.corebanking.service.dto;

public class DateOfBirthUpdateDTO {

    private String phoneNumber;
    private String dateOfBirth;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Override
    public String toString() {
        return "DateOfBirthUpdateDTO{" +
            "phoneNumber='" + phoneNumber + '\'' +
            ", dateOfBirth='" + dateOfBirth + '\'' +
            '}';
    }

}
