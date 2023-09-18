package ng.com.systemspecs.apigateway.service.dto;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

public class UpdateEmailDTO implements Serializable {

    private String phoneNumber;
    private String question;
    private String pin;
    private String answer;
    @NotEmpty(message = "Email cannot be empty")
    private String email;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    @Override
    public String toString() {
        return "ValidateSecretDTO{" +
            "phoneNumber='" + phoneNumber + '\'' +
            ", question='" + question + '\'' +
            ", answer='" + answer + '\'' +
            ", email='" + email + '\'' +
            ", pin='" + pin + '\'' +
            '}';
    }
}
