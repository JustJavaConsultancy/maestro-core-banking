package ng.com.justjava.corebanking.service.dto;


import javax.validation.constraints.NotBlank;

public class LostPinDTO {
    @NotBlank(message = "Phone Number Required")
    private String phoneNumber;
    @NotBlank(message = "New Pin is Required")
    private String newPin;

    private String password;
    private String question;
    private String answer;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getNewPin() {
        return newPin;
    }

    public void setNewPin(String newPin) {
        this.newPin = newPin;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    @Override
    public String toString() {
        return "LostPinDTO{" +
            "phoneNumber='" + phoneNumber + '\'' +
            ", newPin='" + newPin + '\'' +
            ", password='" + password + '\'' +
            ", question='" + question + '\'' +
            ", answer='" + answer + '\'' +
            '}';
    }
}
