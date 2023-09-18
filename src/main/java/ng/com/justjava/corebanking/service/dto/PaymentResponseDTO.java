package ng.com.justjava.corebanking.service.dto;

import org.springframework.http.HttpStatus;

public class PaymentResponseDTO {

    private String message;
    private String code;
    private Boolean error;
    private HttpStatus status;
    private PaymentTransactionDTO paymentTransactionDTO;

    public void setError(Boolean error) {
        this.error = error;
    }

    public PaymentResponseDTO() {
        this.error = false;
    }

    public Boolean getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public PaymentTransactionDTO getPaymentTransactionDTO() {
        return paymentTransactionDTO;
    }

    public void setPaymentTransactionDTO(PaymentTransactionDTO paymentTransactionDTO) {
        this.paymentTransactionDTO = paymentTransactionDTO;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "PaymentResponseDTO [message=" + message + ", code=" + code + ", error=" + error
            + ", paymentTransactionDTO=" + paymentTransactionDTO + "]";
    }
}
