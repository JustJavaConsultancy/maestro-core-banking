package ng.com.justjava.corebanking.service.dto.stp;

import java.io.Serializable;
import java.math.BigDecimal;

public class GetRRRDetailsResponseData implements Serializable {

    private String status;

    private String paymentDate;

    private String rrrNumber;

    private BigDecimal amount;

    private BigDecimal vatAmount;

    private BigDecimal originalAmount;

    private Double chargeFee;

    private Double netAmount;

    private String transactionRef;

    private String paymentMode;

    private String paymentChannel;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getRrrNumber() {
        return rrrNumber;
    }

    public void setRrrNumber(String rrrNumber) {
        this.rrrNumber = rrrNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getVatAmount() {
        return vatAmount;
    }

    public void setVatAmount(BigDecimal vatAmount) {
        this.vatAmount = vatAmount;
    }

    public BigDecimal getOriginalAmount() {
        return originalAmount;
    }

    public void setOriginalAmount(BigDecimal originalAmount) {
        this.originalAmount = originalAmount;
    }

    public Double getChargeFee() {
        return chargeFee;
    }

    public void setChargeFee(Double chargeFee) {
        this.chargeFee = chargeFee;
    }

    public Double getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(Double netAmount) {
        this.netAmount = netAmount;
    }

    public String getTransactionRef() {
        return transactionRef;
    }

    public void setTransactionRef(String transactionRef) {
        this.transactionRef = transactionRef;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getPaymentChannel() {
        return paymentChannel;
    }

    public void setPaymentChannel(String paymentChannel) {
        this.paymentChannel = paymentChannel;
    }
}
