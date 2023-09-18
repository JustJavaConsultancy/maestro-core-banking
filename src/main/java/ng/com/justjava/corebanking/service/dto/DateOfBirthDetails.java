package ng.com.justjava.corebanking.service.dto;

import java.io.Serializable;

public class DateOfBirthDetails implements Serializable {
    private String dob;

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    @Override
    public String toString() {
        return "DateOfBirthDetails{" +
            "dob='" + dob + '\'' +
            '}';
    }
}
