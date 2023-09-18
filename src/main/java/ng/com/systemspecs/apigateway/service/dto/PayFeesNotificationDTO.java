package ng.com.systemspecs.apigateway.service.dto;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class PayFeesNotificationDTO {

    String matricno, paymentRef;
    List<FeesDTO> fees;
    Double amount;
    String payDate;

    public String getMatricno() {
        return matricno;
    }

    public void setMatricno(String matricno) {
        this.matricno = matricno;
    }

    public List<FeesDTO> getFeeid() {
        return fees;
    }

    public void setFees(List<FeesDTO> fees) {
        this.fees = fees;
    }

    public String getPaymentRef() {
        return paymentRef;
    }

    public void setPaymentRef(String paymentRef) {
        this.paymentRef = paymentRef;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getPayDate() {
        return payDate;
    }

    public void setPayDate(String payDate) {
        this.payDate = payDate;
    }

    @Override
    public String toString() {
        return (
            "PayFeesNotificationDTO{" +
                "matricno='" +
                matricno +
                '\'' +
                ", paymentRef='" +
                paymentRef +
                '\'' +
                ", fees=" +
                fees +
                ", amount=" +
                amount +
                ", payDate='" +
                payDate +
                '\'' +
                '}'
        );
    }
}
