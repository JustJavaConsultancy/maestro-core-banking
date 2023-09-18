package ng.com.justjava.corebanking.service.dto;

public class ChangeMyDeviceStatusDTO {

    private String question;
    private String answer;
    private String pin;
    private String status;
    private String deviceId;
    private String phoneNumber;

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

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "ChangeMyDeviceStatusDTO{" +
            "question='" + question + '\'' +
            ", answer='" + answer + '\'' +
            ", pin='" + pin + '\'' +
            ", status='" + status + '\'' +
            ", deviceId='" + deviceId + '\'' +
            ", phoneNumber='" + phoneNumber + '\'' +
            '}';
    }
}
