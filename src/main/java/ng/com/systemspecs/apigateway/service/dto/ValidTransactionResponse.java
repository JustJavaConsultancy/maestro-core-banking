package ng.com.systemspecs.apigateway.service.dto;

public class ValidTransactionResponse {

    private boolean isValid;
    private String message;

    public ValidTransactionResponse(boolean isValid, String message) {
        this.isValid = isValid;
        this.message = message;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ValidTransactionResponse{" +
            "isValid=" + isValid +
            ", message='" + message + '\'' +
            '}';
    }
}
