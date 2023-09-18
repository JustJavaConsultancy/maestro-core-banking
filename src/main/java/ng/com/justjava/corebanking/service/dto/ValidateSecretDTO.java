package ng.com.justjava.corebanking.service.dto;

import java.io.Serializable;

public class ValidateSecretDTO implements Serializable {

    private String phoneNumber;
    private String question;
    private String answer;
    private String otp;
    private Boolean trigger = true;

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

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public Boolean getTrigger() {
        return trigger;
    }

    public void setTrigger(Boolean trigger) {
        this.trigger = trigger;
    }

    @Override
    public String toString() {
        return "ValidateSecretDTO{" +
            "phoneNumber='" + phoneNumber + '\'' +
            ", question='" + question + '\'' +
            ", answer='" + answer + '\'' +
            ", otp='" + otp + '\'' +
            ", trigger=" + trigger +
            '}';
    }
}
