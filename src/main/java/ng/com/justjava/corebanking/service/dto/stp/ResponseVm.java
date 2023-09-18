package ng.com.justjava.corebanking.service.dto.stp;

import java.math.BigDecimal;

public class ResponseVm<T> {

    private String status;


    private BigDecimal amount;

    private String message;

    private T data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResponseVm{" +
            "status='" + status + '\'' +
            ", amount=" + amount +
            ", message='" + message + '\'' +
            ", data=" + data +
            '}';
    }
}
