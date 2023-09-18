package ng.com.justjava.corebanking.service.dto;

public class ValidateBVNDTO {

    private String bvn;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String phoneNumber;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBvn() {
        return bvn;
    }

    public void setBvn(String bvn) {
        this.bvn = bvn;
    }

    @Override
    public String toString() {
        return "BVNPayLOadDTO{" +
            "bvn='" + bvn + '\'' +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", dateOfBirth='" + dateOfBirth + '\'' +
            ", phoneNumber='" + phoneNumber + '\'' +
            '}';
    }
}
